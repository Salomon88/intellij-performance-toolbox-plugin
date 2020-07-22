package org.performancetoolbox.intellij.plugin.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface ToolContentDataLoadable<T> extends PropertyChangeListener {

    T getContentData();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void cancel();

    void execute();

    @Override
    void propertyChange(PropertyChangeEvent evt);
}