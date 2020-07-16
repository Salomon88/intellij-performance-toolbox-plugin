package org.memorytoolbox.intellij.plugin.gcviewer;

import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;

import java.beans.PropertyChangeListener;

public interface ModelLoaderGroupTracker extends PropertyChangeListener {

    String getName();

    void addGcModelLoader(GCModelLoader loader);

    void execute();

    void cancel();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}