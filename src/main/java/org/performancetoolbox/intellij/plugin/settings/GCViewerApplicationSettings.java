package org.performancetoolbox.intellij.plugin.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.tagtraum.perf.gcviewer.util.LocalisationHelper;
import com.tagtraum.perf.gcviewer.view.model.GCPreferences;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.gcviewer.PreferencesComponent;

import static com.intellij.openapi.application.ApplicationManager.getApplication;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import java.awt.Color;

public class GCViewerApplicationSettings implements Configurable {

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel settingsPanel = new JPanel();
        return settingsPanel;
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
