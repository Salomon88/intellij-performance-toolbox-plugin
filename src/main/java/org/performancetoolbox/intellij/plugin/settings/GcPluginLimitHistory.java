package org.performancetoolbox.intellij.plugin.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;

import javax.swing.*;
import java.awt.*;

public abstract class GcPluginLimitHistory extends JPanel implements Configurable {

    private final String componentName;
    protected final int min = Integer.valueOf(GcPluginBundle.getString("settings.min.history.size"));
    protected final int max = Integer.valueOf(GcPluginBundle.getString("settings.max.history.size"));
    protected final int step = Integer.valueOf(GcPluginBundle.getString("settings.history.step.size"));

    public GcPluginLimitHistory(String componentName) {
        this.componentName = componentName;
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JLabel label = new JLabel(componentName);
        JSpinner commonSpinner = new JSpinner(createSpinnerModel());

        mainPanel.add(label);
        mainPanel.add(commonSpinner);
        return mainPanel;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "GcLimits";
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

    protected abstract SpinnerNumberModel createSpinnerModel();
}
