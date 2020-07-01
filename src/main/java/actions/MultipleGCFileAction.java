package actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class MultipleGCFileAction extends CommonAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(true, true, false, false, false, false);
        final VirtualFile virtualFile = FileChooser.chooseFile(descriptor, e.getProject(), null);
        startGcViewer(e.getProject(), virtualFile);
    }
}