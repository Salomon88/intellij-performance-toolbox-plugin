package org.performancetoolbox.intellij.plugin.histogram.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.OpenFileHistoryAdapter;
import org.performancetoolbox.intellij.plugin.common.impl.OpenFileHistoryAdapterPropertiesComponentImpl;
import org.performancetoolbox.intellij.plugin.histogram.OpenFileDialog;

import java.util.List;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE_ARRAY;
import static com.intellij.openapi.actionSystem.IdeActions.GROUP_MAIN_MENU;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.performancetoolbox.intellij.plugin.common.Util.getHistoryRecord;
import static org.performancetoolbox.intellij.plugin.common.Util.getViewerFuncReference;
import static org.performancetoolbox.intellij.plugin.common.factories.ToolContentLoadableFactory.CONTENT_FACTORY;

public class OpenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ofNullable(getVirtualFiles(e)).ifPresent(files -> load(e.getProject(), files));
    }

    private List<VirtualFile> getVirtualFiles(AnActionEvent e) {
        final OpenFileHistoryAdapter historyAdapter = new OpenFileHistoryAdapterPropertiesComponentImpl("performancetoolbox.open.histograms.urls", 15);

        if (GROUP_MAIN_MENU.equals(e.getPlace())) {
            OpenFileDialog dialog = new OpenFileDialog(e.getProject(), historyAdapter);
            return dialog.showAndGet() ? dialog.getResult() : null;
        } else {
            final List<VirtualFile> files = asList(e.getData(VIRTUAL_FILE_ARRAY));
            historyAdapter.addAndStore(getHistoryRecord(files));
            return files;
        }
    }

    private void load(Project project, List<VirtualFile> files) {
        CONTENT_FACTORY
                .getHistInstance(project)
                .load(files, getViewerFuncReference());
    }
}