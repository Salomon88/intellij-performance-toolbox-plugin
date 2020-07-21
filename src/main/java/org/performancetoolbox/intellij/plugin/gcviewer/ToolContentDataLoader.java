package org.performancetoolbox.intellij.plugin.gcviewer;

import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoadable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static javax.swing.SwingWorker.StateValue.DONE;

public class ToolContentDataLoader implements ToolContentDataLoadable<GCResource> {

    private GCModelLoader loader;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public ToolContentDataLoader(GCModelLoader loader) {
        this.loader = loader;
        loader.addPropertyChangeListener(this);
    }

    @Override
    public GCResource getContentData() {
        return loader.getGcResource();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void cancel() {
        if (loader.getGcResource() instanceof GcResourceSeries) {
            for (GCResource resource : ((GcResourceSeries) loader.getGcResource()).getResourcesInOrder()) {
                resource.setIsReadCancelled(true);
            }
        } else {
            loader.getGcResource().setIsReadCancelled(true);
        }

        loader.removePropertyChangeListener(this);
    }

    @Override
    public void execute() {
        loader.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName()) && DONE == evt.getNewValue()) {
            loader.removePropertyChangeListener(this);
        }

        propertyChangeSupport.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
}
