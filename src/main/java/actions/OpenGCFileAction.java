package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.tagtraum.perf.gcviewer.GCViewer;
import org.jetbrains.annotations.NotNull;

public class OpenGCFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new GCViewer().main(new String[]{});
    }
}
