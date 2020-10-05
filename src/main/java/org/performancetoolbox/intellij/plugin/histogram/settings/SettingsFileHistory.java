package org.performancetoolbox.intellij.plugin.histogram.settings;

import org.performancetoolbox.intellij.plugin.common.settings.HistorySettings;

import javax.swing.*;
import java.awt.*;

import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public class SettingsFileHistory extends HistorySettings {


    @Override
    protected Component createHistoryComponent() {
        setLayout(new FlowLayout());
        add(new Label(getString("settings.hist.history.label")));
        add(new JSpinner(new SpinnerNumberModel(1, min, max, step)));
        return this;
    }

    @Override
    protected String getComponentName() {
        return null;
    }
}
