package org.performancetoolbox.intellij.plugin.common.settings;

import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;

import javax.swing.*;

public class ViewerLimitHistorySettings extends MemoryToolBoxLimitHistorySettings {

    public ViewerLimitHistorySettings(String componentText) {
        super(componentText);
    }

    @Override
    protected SpinnerNumberModel createSpinnerModel() {
        return new SpinnerNumberModel(3, min, max, step);
    }

    @Override
    protected String getComponentName() {
        return GcPluginBundle.getString("settings.block.viewer.label");
    }
}
