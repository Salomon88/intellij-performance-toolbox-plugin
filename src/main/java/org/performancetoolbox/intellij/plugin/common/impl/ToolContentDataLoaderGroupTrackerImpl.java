package org.performancetoolbox.intellij.plugin.common.impl;

import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoadable;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoaderGroupTracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.SwingWorker.StateValue.DONE;
import static javax.swing.SwingWorker.StateValue.STARTED;

public class ToolContentDataLoaderGroupTrackerImpl<T> implements ToolContentDataLoaderGroupTracker<T> {

    private final Map<ToolContentDataLoadable<T>, State> progressStates = new HashMap<>();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final String name;
    private int currentProgress = 0;

    public ToolContentDataLoaderGroupTrackerImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addModelLoader(ToolContentDataLoadable<T> loader) {
        loader.addPropertyChangeListener(this);
        progressStates.put(loader, new State(loader));
    }

    @Override
    public void cancel() {
        progressStates.forEach((loader, state) -> loader.cancel());
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
        ToolContentDataLoadable<T> loader = (ToolContentDataLoadable<T>) evt.getSource();
        State state = progressStates.get(loader);

        if ("state".equals(evt.getPropertyName()) && DONE == evt.getNewValue()) {
            state.completedResources = state.totalResources;
            state.partial = 0;
            loader.removePropertyChangeListener(this);
        } else if ("progress".equals(evt.getPropertyName())) {
            if (state.totalResources > 1 && (Integer) evt.getNewValue() < (Integer) evt.getOldValue()) {
                state.completedResources++;
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
        int current = 0;
        int total = 0;

        for (Map.Entry<ToolContentDataLoadable<T>, State> entry : progressStates.entrySet()) {
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
        ToolContentDataLoadable<T> loader;
        int completedResources;
        int partial;
        int totalResources;

        State(ToolContentDataLoadable<T> loader) {
            this.loader = loader;
            this.totalResources = loader.getContentData() instanceof GcResourceSeries
                    ? ((GcResourceSeries) loader.getContentData()).getResourcesInOrder().size()
                    : 1;
        }
    }
}