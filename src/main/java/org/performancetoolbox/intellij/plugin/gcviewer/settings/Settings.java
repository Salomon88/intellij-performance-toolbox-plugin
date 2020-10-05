package org.performancetoolbox.intellij.plugin.gcviewer.settings;

import org.performancetoolbox.intellij.plugin.common.annotations.Parent;
import org.performancetoolbox.intellij.plugin.common.settings.HistorySettings;

import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

@Parent(children = {SettingsFileHistory.class, SettingsTabHistory.class})
public class Settings extends HistorySettings {

    @Override
    protected String getComponentName() {
        return getString("settings.block.viewer.label");
    }
}