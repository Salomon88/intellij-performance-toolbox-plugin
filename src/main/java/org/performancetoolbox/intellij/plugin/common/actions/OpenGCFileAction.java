package org.performancetoolbox.intellij.plugin.common.actions;

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
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.gcviewer.GCDocumentWrapper;
import org.performancetoolbox.intellij.plugin.gcviewer.ModelLoaderController;

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
                {
                    Disposer.register(this, gcDocumentWrapper);
                }

                @Override
                public boolean isContentReuseProhibited() {
                    return true;
                }
            };
            RunContentManager.getInstance(project).showRunContent(executor, descriptor);
        });
    }

    private void startGcViewer(Project project, GCResource gcResource) {
        ModelLoaderController modelLoaderController = new ModelLoaderController(project);
        modelLoaderController.open(gcResource, this::addToConsoleView);
    }
}