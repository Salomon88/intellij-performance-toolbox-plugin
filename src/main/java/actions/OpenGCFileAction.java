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

import static com.intellij.openapi.fileChooser.FileChooser.chooseFile;

public class OpenGCFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final VirtualFile virtualFile = "MainMenu".equals(e.getPlace())
                ? chooseFile(new FileChooserDescriptor(true, true, false, false, false, false), e.getProject(), null)
                : ((VirtualFile) e.getDataContext().getData("virtualFile"));
        startGcViewer(project, virtualFile);
    }

    protected void startGcViewer(Project project, VirtualFile virtualFile) {
        ApplicationManager.getApplication().invokeLater(() -> {
            GCResource gcResource = new GcResourceFile(virtualFile.getPath());
            GCModelLoader gcModelLoader = GCModelLoaderFactory.createFor(gcResource);
            // TODO store in plugin
            GCPreferences gcPreferences = new GCPreferences();
            GCDocument gcDocument = new GCDocument(gcPreferences, gcResource.getResourceName());
            GCDocumentController docController = new GCDocumentController(gcDocument);
            docController.addGCResource(gcModelLoader, new ViewMenuController(null));
            // TODO load with system progress
            gcModelLoader.execute();

            final Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            final ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
            final RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, null, gcDocument, virtualFile.getName(), IconLoader.getIcon("/icons/gcviewer.png")) {
                @Override
                public boolean isContentReuseProhibited() {
                    return true;
                }
            };
            RunContentManager.getInstance(project).showRunContent(executor, descriptor);
        });
    }
}