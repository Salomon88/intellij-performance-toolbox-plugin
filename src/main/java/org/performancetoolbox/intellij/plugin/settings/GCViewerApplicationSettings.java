package org.performancetoolbox.intellij.plugin.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GCViewerApplicationSettings implements Configurable {

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
    }

    @Override
    public void disposeUIResources() {
    }
}