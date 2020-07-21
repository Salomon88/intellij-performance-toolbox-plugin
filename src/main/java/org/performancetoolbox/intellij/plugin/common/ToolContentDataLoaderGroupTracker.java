package org.performancetoolbox.intellij.plugin.common;

import java.beans.PropertyChangeListener;

public interface ToolContentDataLoaderGroupTracker extends PropertyChangeListener {

    String getName();

    void addModelLoader(ToolContentDataLoadable loader);

    void execute();

    void cancel();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}