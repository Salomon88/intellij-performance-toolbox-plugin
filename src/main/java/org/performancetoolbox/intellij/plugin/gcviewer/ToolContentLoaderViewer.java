package org.performancetoolbox.intellij.plugin.gcviewer;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCDocumentController;
import com.tagtraum.perf.gcviewer.ctrl.impl.GCModelLoaderFactory;
import com.tagtraum.perf.gcviewer.ctrl.impl.ViewMenuController;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoaderGroupTracker;
import org.performancetoolbox.intellij.plugin.common.ToolContentHoldable;
import org.performancetoolbox.intellij.plugin.common.ToolContentLoadable;
import org.performancetoolbox.intellij.plugin.common.Util;
import org.performancetoolbox.intellij.plugin.common.impl.ToolContentDataLoaderGroupTrackerImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.BiConsumer;

import static org.performancetoolbox.intellij.plugin.common.Util.doInBackground;
import static org.performancetoolbox.intellij.plugin.common.Util.getNormalizedName;

public class ToolContentLoaderViewer implements ToolContentLoadable<GCResource>, PropertyChangeListener {

    private Project project;

    public ToolContentLoaderViewer(Project project) {
        this.project = project;
    }

    @Override
    public void load(GCResource gcResource, BiConsumer<Project, ToolContentHoldable> callback) {
        ToolContentDataLoaderGroupTracker<GCResource> tracker = new ToolContentDataLoaderGroupTrackerImpl<>(getNormalizedName(gcResource));
        GCModelLoader gcModelLoader = GCModelLoaderFactory.createFor(gcResource);

        PreferenceData preferencesData = ApplicationManager.
                getApplication().
                getComponent(PreferencesComponent.class).
                getPreferenceData(gcResource.getResourceName());

        GCDocument gcDocument = new GCDocument(new PreferencesWrapper(preferencesData), gcResource.getResourceName());
        gcDocument.addPropertyChangeListener(this);
        GCDocumentController docController = new GCDocumentController(gcDocument);
        docController.addGCResource(gcModelLoader, new ViewMenuController(new MockedGCViewerGui()));
        tracker.addModelLoader(new ToolContentDataLoader(gcModelLoader));

        doInBackground(project, tracker, () -> callback.accept(project, new ToolContentHolder(gcDocument, project)));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // used to be found in actions
    }

    public void reload(GCDocument gcDocument) {
        ToolContentDataLoaderGroupTracker<GCResource> tracker = new ToolContentDataLoaderGroupTrackerImpl<>(getNormalizedName(gcDocument));

        for (GCResource gcResource : gcDocument.getGCResources()) {
            if (gcResource.hasUnderlyingResourceChanged()) {
                gcResource.reset();
                gcResource.setIsReload(true);
                GCModelLoader loader = GCModelLoaderFactory.createFor(gcResource);
                GCDocumentController docController = Util.getPropertyChangeListener(gcDocument, GCDocumentController.class);
                docController.reloadGCResource(loader);
                tracker.addModelLoader(new ToolContentDataLoader(loader));
            }
        }

        doInBackground(project, tracker, null);
    }
}