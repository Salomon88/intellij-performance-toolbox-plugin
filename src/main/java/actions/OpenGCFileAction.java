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
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCDocumentController;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCModelLoaderFactory;
import com.tagtraum.perf.gcviewer.ctrl.impl.ViewMenuController;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import com.tagtraum.perf.gcviewer.view.model.GCPreferences;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.util.concurrent.CountDownLatch;

import static com.intellij.openapi.fileChooser.FileChooser.chooseFile;
import static javax.swing.SwingWorker.StateValue.DONE;

public class OpenGCFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        // TODO load series of gc logs
        final VirtualFile virtualFile = "MainMenu".equals(e.getPlace())
                ? chooseFile(new FileChooserDescriptor(true, true, false, false, false, false), e.getProject(), null)
                : ((VirtualFile) e.getDataContext().getData("virtualFile"));
        startGcViewer(project, virtualFile);
    }

    private void addToConsoleView(Project project, GCDocument gcDocument, VirtualFile virtualFile) {
        ApplicationManager.getApplication().invokeLater(() -> {
            final Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            final ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
            final RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, null, gcDocument.getRootPane(), virtualFile.getName(), IconLoader.getIcon("/icons/gcviewer.png")) {
                @Override
                public boolean isContentReuseProhibited() {
                    return true;
                }
            };
            RunContentManager.getInstance(project).showRunContent(executor, descriptor);
        });
    }

    private void waitForParsingCompletion(GCModelLoader gcModelLoader, ProgressIndicator indicator) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final PropertyChangeListener propertyChangeListener = evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                indicator.setFraction(1. / (Integer) evt.getNewValue());
            } else if ("state".equals(evt.getPropertyName()) && DONE == evt.getNewValue()) {
                latch.countDown();
            }
        };
        gcModelLoader.addPropertyChangeListener(propertyChangeListener);
        gcModelLoader.execute();
        latch.await();
        gcModelLoader.removePropertyChangeListener(propertyChangeListener);
    }

    private void startGcViewer(Project project, VirtualFile virtualFile) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ProgressManager.getInstance().run(new Backgroundable(project, "Parsing " + virtualFile.getName(), true) {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    final GCResource gcResource = new GcResourceFile(virtualFile.getPath());
                    final GCModelLoader gcModelLoader = GCModelLoaderFactory.createFor(gcResource);
                    // TODO store in plugin
                    GCPreferences gcPreferences = new GCPreferences();
                    GCDocument gcDocument = new GCDocument(gcPreferences, gcResource.getResourceName());
                    GCDocumentController docController = new GCDocumentController(gcDocument);
                    docController.addGCResource(gcModelLoader, new ViewMenuController(null));

                    try {
                        waitForParsingCompletion(gcModelLoader, indicator);
                        addToConsoleView(project, gcDocument, virtualFile);
                    } catch (InterruptedException e) {/* it is ok on cancel */}
                }
            });
        });
    }
}