package com.github.gcviewerplugin;

import com.github.gcviewerplugin.action.ToggleBooleanAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.util.IconLoader;
import com.tagtraum.perf.gcviewer.ctrl.action.Export;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static com.github.gcviewerplugin.Util.getNormalizedName;
import static com.github.gcviewerplugin.Util.getPropertyChangeListener;
import static com.github.gcviewerplugin.Util.getResourceBundle;
import static com.intellij.icons.AllIcons.Actions.Menu_saveall;
import static com.intellij.icons.AllIcons.Actions.PreviewDetails;
import static com.intellij.icons.AllIcons.Actions.Refresh;
import static com.intellij.icons.AllIcons.General.Settings;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class GCDocumentWrapper {

    private GCDocument gcDocument;
    private JComponent component;

    public GCDocumentWrapper(GCDocument gcDocument) {
        this.gcDocument = gcDocument;
        this.component = initComponent();
    }

    public Icon getIcon() {
        return IconLoader.getIcon("/icons/gcviewer.png");
    }

    public JComponent getComponent() {
        return component;
    }

    public String getDisplayName() {
        return getNormalizedName(gcDocument);
    }

    private JPanel initComponent() {
        final ResourceBundle resourceBundle = getResourceBundle();
        final DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
        defaultActionGroup.add(new AnAction(resourceBundle.getString("action.settings.text"), resourceBundle.getString("action.settings.description"), Settings) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ShowSettingsUtil.getInstance().showSettingsDialog(null, (String) null);
            }
        });
        defaultActionGroup.add(new AnAction(resourceBundle.getString("action.export.text"), resourceBundle.getString("action.export.description"), Menu_saveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                new Export(new MockedGCViewerGui(gcDocument)).actionPerformed(null);
            }
        });
        defaultActionGroup.add(new ToggleBooleanAction(resourceBundle.getString("action.dataPanel.text"), resourceBundle.getString("action.dataPanel.description"), PreviewDetails, false) {
            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                super.setSelected(e, state);
                gcDocument.setShowModelMetricsPanel(!state);
            }
        });
        defaultActionGroup.add(new AnAction(resourceBundle.getString("action.reload.text"), resourceBundle.getString("action.reload.description"), Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ModelLoaderController modelLoaderController = getPropertyChangeListener(gcDocument, ModelLoaderController.class);
                modelLoaderController.reload(gcDocument);
            }
        });
        final ActionToolbar actionBar = ActionManager.getInstance().createActionToolbar("gcview", defaultActionGroup, false);
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(gcDocument.getRootPane(), CENTER);
        jPanel.add(actionBar.getComponent(), WEST);

        return jPanel;
    }
}