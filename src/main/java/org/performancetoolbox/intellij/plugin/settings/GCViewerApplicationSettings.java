package org.performancetoolbox.intellij.plugin.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;

import javax.swing.*;
import java.awt.*;

import static com.intellij.util.ui.UIUtil.DEFAULT_HGAP;
import static com.intellij.util.ui.UIUtil.DEFAULT_VGAP;

public class GCViewerApplicationSettings extends JPanel implements Configurable {
    private GcPluginLimitHistory viewerHistoryLimit;
    private GcPluginLimitHistory histHistoryLimit;

    public GCViewerApplicationSettings() {
        viewerHistoryLimit = new ViewerLimitHistory(GcPluginBundle.getString("settings.viewer.history.label"));
        histHistoryLimit = new HistLimitHistory(GcPluginBundle.getString("settings.hist.history.label"));

        setLayout(new BorderLayout());
        add(createMainComponent());
    }

    private Component createMainComponent() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBag gb = new GridBag()
                .setDefaultInsets(new Insets(0, 0, DEFAULT_VGAP, DEFAULT_HGAP))
                .setDefaultWeightX(1)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.add(viewerHistoryLimit.createComponent(), gb.nextLine().next());
        panel.add(histHistoryLimit.createComponent(), gb.nextLine().next());

        return panel;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return GcPluginBundle.getString("settings.display.text");
    }

    @Override
    public @Nullable JComponent createComponent() {
        return this;
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