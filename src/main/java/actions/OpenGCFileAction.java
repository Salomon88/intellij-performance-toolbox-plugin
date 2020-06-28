package actions;

import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.GCViewer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class OpenGCFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final VirtualFile virtualFile = ((VirtualFile) e.getDataContext().getData("virtualFile"));

        ApplicationManager.getApplication().invokeLater(() -> {
            final JComponent gcViewer = new JPanel();
            gcViewer.add(new JButton("click me"));
            final Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            final ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
            final RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, null, gcViewer, virtualFile.getName(), IconLoader.getIcon("/icons/gcviewer.png")) {
                @Override
                public boolean isContentReuseProhibited() {
                    return true;
                }
            };
            RunContentManager.getInstance(project).showRunContent(executor, descriptor);
        });

        new Thread(() -> {
            try {
                new GCViewer().doMain(new String[]{virtualFile.getCanonicalPath()});
            } catch (InvocationTargetException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
    }
}