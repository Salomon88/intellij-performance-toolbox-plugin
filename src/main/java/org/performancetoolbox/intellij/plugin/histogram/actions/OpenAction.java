package org.performancetoolbox.intellij.plugin.histogram.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.ViewAdderFactory;
import org.performancetoolbox.intellij.plugin.histogram.ToolContentLoader;
import org.performancetoolbox.intellij.plugin.histogram.OpenFileDialog;

import java.util.List;

import static java.util.Optional.ofNullable;

public class OpenAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        OpenFileDialog dialog = new OpenFileDialog(e.getProject());
        ofNullable(dialog.showAndGet() ? dialog.getResult() : null).ifPresent(files -> load(e.getProject(), files));
    }

    private void load(Project project, List<VirtualFile> files) {
        new ToolContentLoader(project).load(files, ViewAdderFactory::addToView);
    }
}