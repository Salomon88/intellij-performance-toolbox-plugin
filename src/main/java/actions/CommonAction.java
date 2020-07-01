package actions;

import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
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

public abstract class CommonAction extends AnAction {

    protected void startGcViewer(Project project, VirtualFile virtualFile) {
        ApplicationManager.getApplication().invokeLater(() -> {
            // TODO store in plugin
            GCResource gcResource = new GcResourceFile(virtualFile.getPath());
            GCModelLoader gcModelLoader = GCModelLoaderFactory.createFor(gcResource);
            GCPreferences gcPreferences = new GCPreferences();
            GCDocument gcDocument = new GCDocument(gcPreferences, gcResource.getResourceName());
            GCDocumentController docController = new GCDocumentController(gcDocument);
            docController.addGCResource(gcModelLoader, new ViewMenuController(null));
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
