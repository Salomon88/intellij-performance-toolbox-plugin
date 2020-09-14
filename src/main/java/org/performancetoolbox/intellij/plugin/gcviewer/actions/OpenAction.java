package org.performancetoolbox.intellij.plugin.gcviewer.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.model.GCResource;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.OpenFileHistoryAdapter;
import org.performancetoolbox.intellij.plugin.common.ViewAdderFactory;
import org.performancetoolbox.intellij.plugin.common.impl.OpenFileHistoryAdapterPropertiesComponentImpl;
import org.performancetoolbox.intellij.plugin.gcviewer.OpenFileDialog;
import org.performancetoolbox.intellij.plugin.gcviewer.ToolContentLoader;

import java.util.Optional;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE_ARRAY;
import static com.intellij.openapi.actionSystem.IdeActions.GROUP_MAIN_MENU;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.performancetoolbox.intellij.plugin.common.Util.getHistoryRecord;
import static org.performancetoolbox.intellij.plugin.common.Util.createGCResource;

public class OpenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        getGCResource(e).ifPresent(gcResource -> load(e.getProject(), gcResource));
    }

    private Optional<GCResource> getGCResource(AnActionEvent e) {
        final OpenFileHistoryAdapter historyAdapter = new OpenFileHistoryAdapterPropertiesComponentImpl("performancetoolbox.open.gc.urls", 15);

        if (GROUP_MAIN_MENU.equals(e.getPlace())) {
            OpenFileDialog dialog = new OpenFileDialog(e.getProject(), historyAdapter);
            return ofNullable(dialog.showAndGet() ? dialog.getResult() : null);
        } else {
            final VirtualFile[] files = e.getData(VIRTUAL_FILE_ARRAY);
            historyAdapter.addAndStore(getHistoryRecord(asList(files)));
            return createGCResource(files);
        }
    }

    private void load(Project project, GCResource gcResource) {
        new ToolContentLoader(project).load(gcResource, ViewAdderFactory::addToView);
    }
}