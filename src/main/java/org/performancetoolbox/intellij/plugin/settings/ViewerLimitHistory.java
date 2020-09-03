package org.performancetoolbox.intellij.plugin.settings;

import javax.swing.*;

public class ViewerLimitHistory extends GcPluginLimitHistory {

    public ViewerLimitHistory(String componentName) {
        super(componentName);
    }

    @Override
    protected SpinnerNumberModel createSpinnerModel() {

        return new SpinnerNumberModel(3, min, max, step);
    }
}
