package org.performancetoolbox.intellij.plugin.settings;

import javax.swing.*;

public class HistLimitHistory extends GcPluginLimitHistory {

    public HistLimitHistory(String componentName) {
        super(componentName);
    }

    @Override
    protected SpinnerNumberModel createSpinnerModel() {
        return new SpinnerNumberModel(1, min, max, step);
    }
}
