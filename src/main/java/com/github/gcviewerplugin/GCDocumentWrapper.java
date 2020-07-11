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
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import static com.github.gcviewerplugin.Util.getNormalizedName;
import static com.github.gcviewerplugin.Util.getPropertyChangeListener;
import static com.github.gcviewerplugin.Util.getResourceBundle;
import static com.intellij.icons.AllIcons.Actions.Menu_saveall;
import static com.intellij.icons.AllIcons.Actions.PreviewDetails;
import static com.intellij.icons.AllIcons.Actions.Refresh;
import static com.intellij.icons.AllIcons.General.Settings;
import static com.intellij.openapi.application.ApplicationManager.getApplication;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.ANTI_ALIAS;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.CONCURRENT_COLLECTION_BEGIN_END;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.FULL_GC_LINES;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.GC_TIMES_LINE;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.GC_TIMES_RECTANGLES;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.INC_GC_LINES;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.INITIAL_MARK_LEVEL;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.SHOW_DATE_STAMP;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.TENURED_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.TOTAL_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.USED_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.USED_TENURED_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.USED_YOUNG_MEMORY;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class GCDocumentWrapper {

    private GCDocument gcDocument;
    private JComponent component;
    private PropertyChangeListener propertyChangeListener;

    public GCDocumentWrapper(GCDocument gcDocument) {
        this.gcDocument = gcDocument;
        this.component = initComponent();
        this.propertyChangeListener = initPropertyChangeListener();
        getApplication().getComponent(PreferencesComponent.class).addPropertyChangeListener(propertyChangeListener);
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

    /**
     * Releases all associated resources on tab close
     */
    public void release() {
        getApplication().getComponent(PreferencesComponent.class).removePropertyChangeListener(propertyChangeListener);
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

    private PropertyChangeListener initPropertyChangeListener() {
        return evt -> {
            switch (evt.getPropertyName()) {
                case ANTI_ALIAS:
                    gcDocument.getModelChart().setAntiAlias((boolean) evt.getNewValue());
                    break;
                case CONCURRENT_COLLECTION_BEGIN_END:
                    gcDocument.getModelChart().setShowConcurrentCollectionBeginEnd((boolean) evt.getNewValue());
                    break;
                case FULL_GC_LINES:
                    gcDocument.getModelChart().setShowFullGCLines((boolean) evt.getNewValue());
                    break;
                case INC_GC_LINES:
                    gcDocument.getModelChart().setShowIncGCLines((boolean) evt.getNewValue());
                    break;
                case INITIAL_MARK_LEVEL:
                    gcDocument.getModelChart().setShowInitialMarkLevel((boolean) evt.getNewValue());
                    break;
                case GC_TIMES_LINE:
                    gcDocument.getModelChart().setShowGCTimesLine((boolean) evt.getNewValue());
                    break;
                case GC_TIMES_RECTANGLES:
                    gcDocument.getModelChart().setShowGCTimesRectangles((boolean) evt.getNewValue());
                    break;
                case SHOW_DATE_STAMP:
                    gcDocument.getModelChart().setShowDateStamp((boolean) evt.getNewValue());
                    break;
                case TENURED_MEMORY:
                    gcDocument.getModelChart().setShowTenured((boolean) evt.getNewValue());
                    break;
                case TOTAL_MEMORY:
                    gcDocument.getModelChart().setShowTotalMemoryLine((boolean) evt.getNewValue());
                    break;
                case USED_MEMORY:
                    gcDocument.getModelChart().setShowUsedMemoryLine((boolean) evt.getNewValue());
                    break;
                case USED_TENURED_MEMORY:
                    gcDocument.getModelChart().setShowUsedTenuredMemoryLine((boolean) evt.getNewValue());
                    break;
                case USED_YOUNG_MEMORY:
                    gcDocument.getModelChart().setShowUsedYoungMemoryLine((boolean) evt.getNewValue());
                    break;
                default:
                    break;
            }
        };
    }
}