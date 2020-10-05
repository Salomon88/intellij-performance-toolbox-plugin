package org.performancetoolbox.intellij.plugin.histogram.settings;

import org.performancetoolbox.intellij.plugin.common.annotations.Parent;
import org.performancetoolbox.intellij.plugin.common.settings.HistorySettings;

import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

@Parent(children = {SettingsFileHistory.class})
public class Settings extends HistorySettings {

    @Override
    protected String getComponentName() {
        return getString("settings.block.hist.label");
    }
}