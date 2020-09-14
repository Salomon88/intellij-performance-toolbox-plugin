package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;

import javax.swing.*;
import java.awt.*;

public class MainToolBoxPanel extends JPanel implements Configurable {

    private static final JBInsets INSET = JBUI.insets(2, 2, 0, 2);

    private MemoryToolBoxLimitHistorySettings viewerHistoryLimit;
    private MemoryToolBoxLimitHistorySettings histHistoryLimit;

    public MainToolBoxPanel() {
        viewerHistoryLimit = new ViewerLimitHistorySettings(GcPluginBundle.getString("settings.viewer.history.label"));
        histHistoryLimit = new HistLimitHistorySettings(GcPluginBundle.getString("settings.hist.history.label"));
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
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = INSET;
        add(viewerHistoryLimit.createComponent(), gbc);

        gbc.gridy = 1;
        gbc.insets = INSET;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1d;
        gbc.weighty = 1D;
        add(histHistoryLimit.createComponent(), gbc);
    }

}
