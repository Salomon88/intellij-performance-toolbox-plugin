package org.performancetoolbox.intellij.plugin.histogram.settings;

import org.performancetoolbox.intellij.plugin.common.settings.HistorySettings;

import javax.swing.*;

import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public class Settings extends HistorySettings {

    public Settings() {
        this(getString("settings.hist.history.label"));
    }

    public Settings(String componentText) {
        super(componentText);
    }

    @Override
    protected SpinnerNumberModel createSpinnerModel() {
        return new SpinnerNumberModel(1, min, max, step);
    }

    @Override
    protected String getComponentName() {
        return getString("settings.block.hist.label");
    }
}