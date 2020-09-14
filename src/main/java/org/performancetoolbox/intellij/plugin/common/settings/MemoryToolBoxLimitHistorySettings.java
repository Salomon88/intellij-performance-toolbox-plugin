package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;

import javax.swing.*;
import java.awt.*;

public abstract class MemoryToolBoxLimitHistorySettings extends JPanel implements Configurable {

    private final String componentText;
    protected final int min = Integer.valueOf(GcPluginBundle.getString("settings.min.history.size"));
    protected final int max = Integer.valueOf(GcPluginBundle.getString("settings.max.history.size"));
    protected final int step = Integer.valueOf(GcPluginBundle.getString("settings.history.step.size"));

    public MemoryToolBoxLimitHistorySettings(String componentText) {
        this.componentText = componentText;
        createMainComponent();
    }

    @Override
    public JComponent createComponent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder(getComponentName()));

        JLabel label = new JLabel(componentText);
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

    private void createMainComponent() {
        JLabel label = new JLabel(componentText);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JSpinner commonSpinner = new JSpinner(createSpinnerModel());
        add(label);
        add(commonSpinner);
    }

    protected abstract SpinnerNumberModel createSpinnerModel();

    protected abstract String getComponentName();
}
