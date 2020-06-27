package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.GCViewer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class OpenGCFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new Thread(() -> {
            try {
                new GCViewer().doMain(new String[]{((VirtualFile) e.getDataContext().getData("virtualFile")).getCanonicalPath()});
            } catch (InvocationTargetException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
    }
}