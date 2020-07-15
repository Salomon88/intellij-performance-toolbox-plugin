package prefs;

import com.github.gcviewerplugin.Util;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GCViewerApplicationSettings implements Configurable {

    public GCViewerApplicationSettings() {

    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return Util.getResourceBundle().getString("action.settings.window.name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        JComponent jComponent = new JPanel();
        return jComponent;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
