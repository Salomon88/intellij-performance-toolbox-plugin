package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public class ApplicationSettings implements Configurable {

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return getString("settings.description.tooltip");
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel myMainPanel = new JPanel(new BorderLayout());
        myMainPanel.add(new MainToolBoxPanel().createComponent(), BorderLayout.WEST);
        return myMainPanel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() {
    }

    @Override
    public void disposeUIResources() {
    }
}