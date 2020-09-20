package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST;
import static com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW;
import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;
import static javax.swing.Box.createVerticalGlue;
import static org.performancetoolbox.intellij.plugin.common.Util.getSubTypesOf;

public class MainToolBoxPanel extends JPanel implements Configurable {

    public MainToolBoxPanel() {
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return null;
    }

    @Override
    public @Nullable JComponent createComponent() {
        createMainComponent();
        return this;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() {
    }

    private void createMainComponent() {
        List<Class<? extends HistorySettings>> classes = new ArrayList<>(getSubTypesOf(HistorySettings.class));

        if (!classes.isEmpty()) {
            setLayout(new GridLayoutManager(classes.size() + 1, 1));
            range(0, classes.size()).forEach(index -> {
                try {
                    add(requireNonNull(classes.get(index).getConstructor().newInstance().createComponent()), getGridConstraints(index, SIZEPOLICY_FIXED));
                } catch (Exception e) {
                    /* ignored for now */
                }
            });
            add(createVerticalGlue(), getGridConstraints(classes.size(), SIZEPOLICY_CAN_GROW | SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_WANT_GROW));
        }
    }

    private GridConstraints getGridConstraints(int row, int vSizePolicy) {
        return new GridConstraints(
                row, 0, 1, 1, ANCHOR_WEST, FILL_BOTH, SIZEPOLICY_CAN_GROW | SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_WANT_GROW, vSizePolicy,
                new Dimension(-1, -1),
                new Dimension(-1, -1),
                new Dimension(-1, -1),
                0, false
        );
    }
}