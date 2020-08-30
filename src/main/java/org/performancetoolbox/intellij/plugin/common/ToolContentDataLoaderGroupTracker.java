package org.performancetoolbox.intellij.plugin.common;

import java.beans.PropertyChangeListener;

public interface ToolContentDataLoaderGroupTracker<T> extends PropertyChangeListener {

    String getName();

    void addModelLoader(ToolContentDataLoadable<T> loader);

    void execute();

    void cancel();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}