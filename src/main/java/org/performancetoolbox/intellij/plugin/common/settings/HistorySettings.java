package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.awt.*;

import static com.intellij.ui.IdeBorderFactory.createTitledBorder;
import static com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST;
import static com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH;
import static com.intellij.uiDesigner.core.GridConstraints.FILL_NONE;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;
import static javax.swing.Box.createHorizontalGlue;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public abstract class HistorySettings implements Configurable {

    private final String componentText;
    protected final int min = Integer.valueOf(getString("settings.min.history.size"));
    protected final int max = Integer.valueOf(getString("settings.max.history.size"));
    protected final int step = Integer.valueOf(getString("settings.history.step.size"));

    public HistorySettings(String componentText) {
        this.componentText = componentText;
    }

    @Override
    public JComponent createComponent() {
        JPanel mainPanel = new JPanel(new GridLayoutManager(1, 3));
        mainPanel.setBorder(createTitledBorder(getComponentName()));

        JLabel label = new JLabel(componentText);
        mainPanel.add(label, getGridConstraints(0));

        JSpinner commonSpinner = new JSpinner(createSpinnerModel());
        mainPanel.add(commonSpinner, getGridConstraints(1));
        mainPanel.add(createHorizontalGlue(), getGridConstraints(2));

        return mainPanel;
    }

    private GridConstraints getGridConstraints(int column) {
        return new GridConstraints(
                0, column, 1, 1, ANCHOR_WEST, column == 2 ? FILL_BOTH : FILL_NONE, column == 2 ? SIZEPOLICY_CAN_GROW | SIZEPOLICY_CAN_SHRINK : SIZEPOLICY_FIXED, SIZEPOLICY_FIXED,
                new Dimension(-1, -1),
                new Dimension(-1, -1),
                new Dimension(-1, -1),
                0, false
        );
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

    protected abstract String getComponentName();
}