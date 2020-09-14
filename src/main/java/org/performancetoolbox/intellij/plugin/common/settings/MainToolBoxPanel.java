package org.performancetoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (Class<? extends HistorySettings> clazz : getSubTypesOf(HistorySettings.class)) {
            try {
                add(clazz.getConstructor().newInstance().createComponent());
            } catch (Exception e) {
                /* ignored for now */
            }
        }
    }
}