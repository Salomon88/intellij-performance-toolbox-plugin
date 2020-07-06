package actions;

import com.github.gcviewerplugin.GCDocumentWrapper;
import com.github.gcviewerplugin.GCModelLoaderController;
import com.github.gcviewerplugin.MockedGCViewerGui;
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
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCDocumentController;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCModelLoaderFactory;
import com.tagtraum.perf.gcviewer.ctrl.impl.ViewMenuController;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import com.tagtraum.perf.gcviewer.view.model.GCPreferences;
import org.jetbrains.annotations.NotNull;

import static com.github.gcviewerplugin.Util.getNormalizedName;
import static com.intellij.openapi.actionSystem.IdeActions.GROUP_MAIN_MENU;
import static com.intellij.openapi.fileChooser.FileChooser.chooseFiles;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class OpenGCFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ofNullable(getGCResource(e)).ifPresent(gcResource -> startGcViewer(e.getProject(), gcResource));
    }

    private GCResource getGCResource(AnActionEvent e) {
        if (GROUP_MAIN_MENU.equals(e.getPlace())) {
            FileChooserDescriptor fcd = new FileChooserDescriptor(true, false, false, false, false, true);
            VirtualFile[] virtualFiles = chooseFiles(fcd, e.getProject(), null);
            return getGCResource(virtualFiles);
        }

        return getGCResource(((VirtualFile) e.getDataContext().getData("virtualFile")));
    }

    private GCResource getGCResource(VirtualFile... virtualFiles) {
        if (virtualFiles == null || virtualFiles.length == 0) {
            return null;
        } else if (virtualFiles.length == 1) {
            return new GcResourceFile(virtualFiles[0].getPath());
        }

        return new GcResourceSeries(stream(virtualFiles).map(VirtualFile::getPath).sorted(reverseOrder()).map(GcResourceFile::new).collect(toList()));
    }

    private void addToConsoleView(Project project, GCDocument gcDocument) {
        ApplicationManager.getApplication().invokeLater(() -> {
            final Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            final ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
            final GCDocumentWrapper gcDocumentWrapper = new GCDocumentWrapper(gcDocument);
            final RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, null, gcDocumentWrapper.getComponent(), gcDocumentWrapper.getDisplayName(), gcDocumentWrapper.getIcon()) {
                @Override
                public boolean isContentReuseProhibited() {
                    return true;
                }
            };
            RunContentManager.getInstance(project).showRunContent(executor, descriptor);
        });
    }

    private void startGcViewer(Project project, GCResource gcResource) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ProgressManager.getInstance().run(new Backgroundable(project, "Parsing " + getNormalizedName(gcResource), true) {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    final GCModelLoader gcModelLoader = GCModelLoaderFactory.createFor(gcResource);
                    // TODO store in plugin
                    GCPreferences gcPreferences = new GCPreferences();
                    GCDocument gcDocument = new GCDocument(gcPreferences, gcResource.getResourceName());
                    GCDocumentController docController = new GCDocumentController(gcDocument);
                    docController.addGCResource(gcModelLoader, new ViewMenuController(new MockedGCViewerGui()));

                    try {
                        new GCModelLoaderController(gcModelLoader, gcResource, indicator).run();
                        addToConsoleView(project, gcDocument);
                    } catch (InterruptedException e) {/* it is ok on cancel */}
                }
            });
        });
    }
}