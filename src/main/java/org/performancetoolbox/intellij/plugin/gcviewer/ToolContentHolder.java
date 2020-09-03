package org.performancetoolbox.intellij.plugin.gcviewer;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.tagtraum.perf.gcviewer.ctrl.action.Export;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import com.tagtraum.perf.gcviewer.view.ModelChart;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.ToolContentHoldable;
import org.performancetoolbox.intellij.plugin.common.actions.ToggleBooleanAction;
import org.performancetoolbox.intellij.plugin.common.bundles.GcPluginBundle;
import org.performancetoolbox.intellij.plugin.gcviewer.actions.ToggleZoomAction;
import org.performancetoolbox.intellij.plugin.gcviewer.actions.ViewAction;
import org.performancetoolbox.intellij.plugin.settings.GCViewerApplicationSettings;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

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
import static org.performancetoolbox.intellij.plugin.common.Util.getNormalizedName;
import static org.performancetoolbox.intellij.plugin.common.Util.getPropertyChangeListener;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class ToolContentHolder implements ToolContentHoldable {

    private final Project project;
    private GCDocument gcDocument;
    private JComponent component;

    public ToolContentHolder(GCDocument gcDocument, Project project) {
        this.gcDocument = gcDocument;
        this.component = initComponent();
        this.project = project;

        PreferencesComponent prefData = getApplication().
                getComponent(PreferencesComponent.class);
        GCResource gcResource = gcDocument.getGCResources().get(0);
        prefData.setGcDocPreference(gcResource.getResourceName(), initPropertyChangeListener());
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

    public ModelChart getModelChart() {
        return gcDocument.getModelChart();
    }

    /**
     * Releases all associated resources on tab close
     */
    @Override
    public void dispose() {
        getApplication().
                getComponent(PreferencesComponent.class).
                removeGcDocListener(gcDocument.getGCResources().get(0).getResourceName());
    }

    private JPanel initComponent() {
        final GcPluginBundle resourceBundle = getResourceBundle();
        final DefaultActionGroup defaultActionGroup = new DefaultActionGroup();

        defaultActionGroup.add(new AnAction(resourceBundle.getString("settings.display.text"), resourceBundle.getString("settings.description.tooltip"), Settings) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, GCViewerApplicationSettings.class);
            }
        });
        defaultActionGroup.add(new AnAction(resourceBundle.getString("action.gc.export.text"), resourceBundle.getString("action.gc.export.description"), Menu_saveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                new Export(new MockedGCViewerGui(gcDocument)).actionPerformed(null);
            }
        });
        defaultActionGroup.add(new ToggleBooleanAction(resourceBundle.getString("action.gc.dataPanel.text"), resourceBundle.getString("action.gc.dataPanel.description"), PreviewDetails, false) {
            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                super.setSelected(e, state);
                gcDocument.setShowModelMetricsPanel(!state);
            }
        });
        defaultActionGroup.add(new AnAction(resourceBundle.getString("action.gc.reload.text"), resourceBundle.getString("action.gc.reload.description"), Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ToolContentLoaderViewer modelLoaderController = getPropertyChangeListener(gcDocument, ToolContentLoaderViewer.class);
                modelLoaderController.reload(gcDocument);
            }
        });

        defaultActionGroup.add(new ToggleZoomAction.ZoomActionGroup(this));

        PreferenceData preferencesData = ApplicationManager.
                getApplication().
                getComponent(PreferencesComponent.class).
                getPreferenceData(gcDocument.getGCResources().get(0).getResourceName());

        defaultActionGroup.add(new ViewAction.ViewActionGroup(preferencesData));

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