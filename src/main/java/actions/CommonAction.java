package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.GCViewer;

import java.lang.reflect.InvocationTargetException;

public abstract class CommonAction extends AnAction {

   protected void startGcViewer(VirtualFile virtualFile) {
        new Thread(() -> {
            try {
                new GCViewer().doMain(new String[]{virtualFile.getCanonicalPath()});
            } catch (InvocationTargetException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
    }
}
