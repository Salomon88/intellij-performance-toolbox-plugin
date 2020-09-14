package org.performancetoolbox.intellij.plugin.common.settings;

import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;

import javax.swing.*;

public class HistLimitHistorySettings extends MemoryToolBoxLimitHistorySettings {


    public HistLimitHistorySettings(String componentText) {
        super(componentText);
    }

    @Override
    protected SpinnerNumberModel createSpinnerModel() {
        return new SpinnerNumberModel(1, min, max, step);
    }

    @Override
    protected String getComponentName() {
        return GcPluginBundle.getString("settings.block.hist.label");
    }
}
