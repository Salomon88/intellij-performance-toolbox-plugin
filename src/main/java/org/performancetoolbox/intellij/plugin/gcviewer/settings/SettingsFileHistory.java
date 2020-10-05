package org.performancetoolbox.intellij.plugin.gcviewer.settings;

import org.performancetoolbox.intellij.plugin.common.settings.HistorySettings;
import org.performancetoolbox.intellij.plugin.gcviewer.PreferencesComponent;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.application.ApplicationManager.getApplication;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public class SettingsFileHistory extends HistorySettings {

    PreferencesComponent preferences = getApplication()
            .getComponent(PreferencesComponent.class);

    @Override
    protected Component createHistoryComponent() {
        setLayout(new FlowLayout());
        add(new JLabel(getString("settings.viewer.file.history.label")));
        add(new JSpinner(new SpinnerNumberModel(3, min, max, step)));
        return this;
    }


    @Override
    protected String getComponentName() {
        return null;
    }

}
