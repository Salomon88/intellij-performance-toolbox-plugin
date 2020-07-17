package org.memorytoolbox.intellij.plugin.gcviewer.impl;

import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import org.memorytoolbox.intellij.plugin.gcviewer.ModelLoaderGroupTracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.SwingWorker.StateValue.DONE;
import static javax.swing.SwingWorker.StateValue.STARTED;

public class ModelLoaderGroupTrackerImpl implements ModelLoaderGroupTracker {

    private final Map<GCModelLoader, State> progressStates = new HashMap<>();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final String name;
    private int currentProgress = 0;

    public ModelLoaderGroupTrackerImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addGcModelLoader(GCModelLoader loader) {
        loader.addPropertyChangeListener(this);
        progressStates.put(loader, new State(loader));
    }

    @Override
    public void cancel() {
        progressStates.forEach((loader, state) -> {
            GCResource gcResource = loader.getGcResource();

            if (gcResource instanceof GcResourceSeries) {
                for (GCResource resource : ((GcResourceSeries) gcResource).getResourcesInOrder()) {
                    resource.setIsReadCancelled(true);
                }
            } else {
                gcResource.setIsReadCancelled(true);
            }
        });
    }

    @Override
    public void execute() {
        if (progressStates.isEmpty()) {
            fireStateDone();
        } else {
            progressStates.forEach((loader, state) -> loader.execute());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GCModelLoader loader = (GCModelLoader) evt.getSource();
        State state = progressStates.get(loader);

        if ("state".equals(evt.getPropertyName()) && DONE == evt.getNewValue()) {
            state.completedResources = state.totalResources;
            state.partial = 0;
            loader.removePropertyChangeListener(this);
        } else if ("progress".equals(evt.getPropertyName())) {
            if (state.totalResources > 1) {
                if ((Integer) evt.getNewValue() < (Integer) evt.getOldValue()) {
                    state.completedResources++;
                }
            }

            state.partial = (Integer) evt.getNewValue();
        }

        fireProgressChanged();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void fireProgressChanged() {
        int current = 0, total = 0;

        for (Map.Entry<GCModelLoader, State> entry : progressStates.entrySet()) {
            State state = entry.getValue();
            current += 100 * state.completedResources + state.partial;
            total += 100 * state.totalResources;
        }

        if (current == total) {
            fireStateDone();
        } else {
            int newProgress = 100 * current / total;
            propertyChangeSupport.firePropertyChange("progress", currentProgress, newProgress);
            currentProgress = newProgress;
        }
    }

    private void fireStateDone() {
        propertyChangeSupport.firePropertyChange("state", STARTED, DONE);
    }

    private class State {
        GCModelLoader loader;
        int completedResources;
        int partial;
        int totalResources;

        State(GCModelLoader loader) {
            this.loader = loader;
            this.totalResources = loader.getGcResource() instanceof GcResourceSeries
                    ? ((GcResourceSeries) loader.getGcResource()).getResourcesInOrder().size()
                    : 1;
        }
    }
}