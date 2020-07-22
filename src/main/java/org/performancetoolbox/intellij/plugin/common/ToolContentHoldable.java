package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.Disposable;

import javax.swing.*;

public interface ToolContentHoldable extends Disposable {

    Icon getIcon();

    JComponent getComponent();

    String getDisplayName();
}