package org.performancetoolbox.intellij.plugin.histogram.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.ViewAdderFactory;
import org.performancetoolbox.intellij.plugin.histogram.OpenFileDialog;
import org.performancetoolbox.intellij.plugin.histogram.ToolContentLoader;

import java.util.List;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE_ARRAY;
import static com.intellij.openapi.actionSystem.IdeActions.GROUP_MAIN_MENU;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class OpenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ofNullable(getVirtualFiles(e)).ifPresent(files -> load(e.getProject(), files));
    }

    private List<VirtualFile> getVirtualFiles(AnActionEvent e) {
        if (GROUP_MAIN_MENU.equals(e.getPlace())) {
            OpenFileDialog dialog = new OpenFileDialog(e.getProject());
            return dialog.showAndGet() ? dialog.getResult() : null;
        }

        return asList(e.getData(VIRTUAL_FILE_ARRAY));
    }

    private void load(Project project, List<VirtualFile> files) {
        new ToolContentLoader(project).load(files, ViewAdderFactory::addToView);
    }
}