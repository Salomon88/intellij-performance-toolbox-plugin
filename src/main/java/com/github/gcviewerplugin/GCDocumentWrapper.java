package com.github.gcviewerplugin;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.util.IconLoader;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.github.gcviewerplugin.Util.getNormalizedName;

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
        return getNormalizedName(gcDocument.getGCResources().get(0));
    }

    private JPanel initComponent() {
        final DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
        defaultActionGroup.add(new AnAction("Settings", "Open plugin settings", IconLoader.getIcon("/general/settings.png")) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ShowSettingsUtil.getInstance().showSettingsDialog(null, (String) null);
            }
        });
        defaultActionGroup.add(new AnAction("Reload", "Reload the current file", IconLoader.getIcon("/actions/refresh.png")) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
            }
        });
        final ActionToolbar actionBar = ActionManager.getInstance().createActionToolbar("gcview", defaultActionGroup, false);

        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(gcDocument.getRootPane(), BorderLayout.CENTER);
        jPanel.add(actionBar.getComponent(), BorderLayout.WEST);

        return jPanel;
    }
}