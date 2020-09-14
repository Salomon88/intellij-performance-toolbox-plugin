package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;

import javax.swing.*;
import java.awt.*;

public class GCViewerApplicationSettings implements Configurable {

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return GcPluginBundle.getString("settings.description.tooltip");
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