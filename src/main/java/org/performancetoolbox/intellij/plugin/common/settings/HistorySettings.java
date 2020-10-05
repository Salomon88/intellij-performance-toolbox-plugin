package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nls;
import org.performancetoolbox.intellij.plugin.common.annotations.Parent;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static com.intellij.ui.IdeBorderFactory.createTitledBorder;
import static com.intellij.uiDesigner.core.GridConstraints.*;
import static javax.swing.Box.createHorizontalGlue;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public abstract class HistorySettings extends JPanel implements Configurable {

    protected final int min = Integer.valueOf(getString("settings.min.history.size"));
    protected final int max = Integer.valueOf(getString("settings.max.history.size"));
    protected final int step = Integer.valueOf(getString("settings.history.step.size"));

    @Override
    public JComponent createComponent() {
        JPanel mainPanel = new JPanel(new GridLayoutManager(1, 3));
        if (this.getClass().isAnnotationPresent(Parent.class)) {
            mainPanel.setBorder(createTitledBorder(getComponentName()));
        }

        mainPanel.add(createHistoryComponent(), getGridConstraints(1));
        mainPanel.add(createHorizontalGlue(), getGridConstraints(2));

        return mainPanel;
    }

    private GridConstraints getGridConstraints(int column) {
        return new GridConstraints(
                0,
                column,
                1,
                1,
                ANCHOR_WEST,
                column == 2 ? FILL_BOTH : FILL_NONE,
                column == 2 ? SIZEPOLICY_CAN_GROW | SIZEPOLICY_CAN_SHRINK : SIZEPOLICY_FIXED,
                SIZEPOLICY_FIXED,
                new Dimension(-1, -1),
                new Dimension(-1, -1),
                new Dimension(-1, -1),
                0,
                false
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

    protected Component createHistoryComponent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Parent parent = (Parent) Arrays.stream(
                this.getClass().getAnnotations())
                .filter(type -> type.annotationType() == Parent.class)
                .findFirst()
                .get();

        for (Class<? extends HistorySettings> clazz : parent.children()) {
            try {
                add(clazz.getConstructor().newInstance().createComponent());
            } catch (Exception e) {
                /* ignored for now */
            }
        }

        return this;
    }

    protected abstract String getComponentName();
}