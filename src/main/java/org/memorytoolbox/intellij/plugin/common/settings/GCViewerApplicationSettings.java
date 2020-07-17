package org.memorytoolbox.intellij.plugin.common.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.tagtraum.perf.gcviewer.util.LocalisationHelper;
import com.tagtraum.perf.gcviewer.view.model.GCPreferences;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.memorytoolbox.intellij.plugin.gcviewer.PreferencesComponent;

import static com.intellij.openapi.application.ApplicationManager.getApplication;
import static org.memorytoolbox.intellij.plugin.common.Util.getResourceBundle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import java.awt.Color;

public class GCViewerApplicationSettings implements Configurable {

    private PreferencesComponent prefs;
    private JCheckBox menuItemIncGCLines;
    private JCheckBox menuItemGcTimesLine;

    public GCViewerApplicationSettings() {
        prefs = getApplication().getComponent(PreferencesComponent.class);
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return getResourceBundle().getString("action.settings.window.name");
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new VerticalLayout());
        settingsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        menuItemIncGCLines = new JCheckBox(LocalisationHelper.getString("main_frame_menuitem_inc_gc_lines"),prefs.isIncGcLines());
        menuItemIncGCLines.setMnemonic(LocalisationHelper.getString("main_frame_menuitem_mnemonic_inc_gc_lines").charAt(0));
        menuItemIncGCLines.setActionCommand(GCPreferences.INC_GC_LINES);
//        menuItemIncGCLines.setBackground(Color.BLACK);
        settingsPanel.add(GCPreferences.INC_GC_LINES, menuItemIncGCLines);

        menuItemGcTimesLine = new JCheckBox(LocalisationHelper.getString("main_frame_menuitem_gc_times_line"),prefs.isGcTimesLine());
        menuItemGcTimesLine.setMnemonic(LocalisationHelper.getString("main_frame_menuitem_mnemonic_gc_times_line").charAt(0));
        menuItemGcTimesLine.setToolTipText(LocalisationHelper.getString("main_frame_menuitem_hint_gc_times_line"));
        //        menuItemGcTimesLine.setBackground(Color.CYAN);
        settingsPanel.add(GCPreferences.GC_TIMES_LINE, menuItemGcTimesLine);

        return settingsPanel;
    }

    @Override
    public boolean isModified() {
        return
                menuItemIncGCLines.isSelected() != prefs.isIncGcLines() ||
                        menuItemGcTimesLine.isSelected() != prefs.isGcTimesLine();

    }

    @Override
    public void apply() throws ConfigurationException {
        prefs.setIncGcLines(menuItemIncGCLines.isSelected());
        prefs.setGcTimesLine(menuItemGcTimesLine.isSelected());
//        prefs = getApplication().getComponent(PreferencesComponent.class);
    }

    @Override
    public void disposeUIResources() {
        if (isModified()) {
            try {
                apply();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}
