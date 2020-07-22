package org.performancetoolbox.intellij.plugin.gcviewer.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.model.GCResource;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.ViewAdderFactory;
import org.performancetoolbox.intellij.plugin.gcviewer.ToolContentLoader;
import org.performancetoolbox.intellij.plugin.gcviewer.OpenFileDialog;

import static com.intellij.openapi.actionSystem.IdeActions.GROUP_MAIN_MENU;
import static java.util.Optional.ofNullable;
import static org.performancetoolbox.intellij.plugin.common.Util.createGCResource;

public class OpenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ofNullable(getGCResource(e)).ifPresent(gcResource -> load(e.getProject(), gcResource));
    }

    private GCResource getGCResource(AnActionEvent e) {
        if (GROUP_MAIN_MENU.equals(e.getPlace())) {
            OpenFileDialog dialog = new OpenFileDialog(e.getProject());
            return dialog.showAndGet() ? dialog.getResult() : null;
        }

        return createGCResource(((VirtualFile) e.getDataContext().getData("virtualFile")));
    }

    private void load(Project project, GCResource gcResource) {
        new ToolContentLoader(project).load(gcResource, ViewAdderFactory::addToView);
    }
}