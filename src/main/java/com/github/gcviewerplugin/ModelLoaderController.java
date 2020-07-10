package com.github.gcviewerplugin;

import com.github.gcviewerplugin.impl.ModelLoaderGroupTrackerImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCDocumentController;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCModelLoaderFactory;
import com.tagtraum.perf.gcviewer.ctrl.impl.ViewMenuController;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

import static com.github.gcviewerplugin.Util.getNormalizedName;
import static com.github.gcviewerplugin.Util.getPropertyChangeListener;
import static com.github.gcviewerplugin.Util.getResourceBundle;
import static java.lang.String.format;
import static javax.swing.SwingWorker.StateValue.DONE;

public class ModelLoaderController implements PropertyChangeListener {

    private Project project;

    public ModelLoaderController(Project project) {
        this.project = project;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // used to be found in actions
    }

    public void open(GCResource gcResource, BiConsumer<Project, GCDocument> consoleViewAdder) {
        ModelLoaderGroupTracker tracker = new ModelLoaderGroupTrackerImpl(getNormalizedName(gcResource));
        GCModelLoader gcModelLoader = GCModelLoaderFactory.createFor(gcResource);
        PreferencesComponent preferencesComponent = ApplicationManager.getApplication().getComponent(PreferencesComponent.class);
        GCDocument gcDocument = new GCDocument(new Preferences(preferencesComponent), gcResource.getResourceName());
        gcDocument.addPropertyChangeListener(this);
        GCDocumentController docController = new GCDocumentController(gcDocument);
        docController.addGCResource(gcModelLoader, new ViewMenuController(new MockedGCViewerGui()));
        tracker.addGcModelLoader(gcModelLoader);

        doInBackground(tracker, () -> consoleViewAdder.accept(project, gcDocument));
    }

    public void reload(GCDocument gcDocument) {
        ModelLoaderGroupTracker tracker = new ModelLoaderGroupTrackerImpl(getNormalizedName(gcDocument));

        for (GCResource gcResource : gcDocument.getGCResources()) {
            if (gcResource.hasUnderlyingResourceChanged()) {
                gcResource.reset();
                gcResource.setIsReload(true);
                GCModelLoader loader = GCModelLoaderFactory.createFor(gcResource);
                GCDocumentController docController = getPropertyChangeListener(gcDocument, GCDocumentController.class);
                docController.reloadGCResource(loader);
                tracker.addGcModelLoader(loader);
            }
        }

        doInBackground(tracker, null);
    }

    private void doInBackground(ModelLoaderGroupTracker tracker, Runnable successAction) {
        final ResourceBundle resourceBundle = getResourceBundle();
        ApplicationManager.getApplication().invokeLater(() -> ProgressManager.getInstance().run(new Backgroundable(project, format(resourceBundle.getString("parsing.text"), tracker.getName()), true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                final CountDownLatch latch = new CountDownLatch(1);
                final PropertyChangeListener propertyChangeListener = evt -> {
                    if (indicator.isCanceled() || "state".equals(evt.getPropertyName()) && DONE == evt.getNewValue()) {
                        latch.countDown();
                    } else if ("progress".equals(evt.getPropertyName())) {
                        indicator.setFraction((Integer) evt.getNewValue() / 100.);
                    }
                };

                try {
                    tracker.addPropertyChangeListener(propertyChangeListener);
                    tracker.execute();
                    latch.await();
                    indicator.checkCanceled();

                    if (successAction != null) {
                        successAction.run();
                    }
                } catch (Exception e) {
                    tracker.cancel();
                } finally {
                    tracker.removePropertyChangeListener(propertyChangeListener);
                }
            }
        }));
    }
}