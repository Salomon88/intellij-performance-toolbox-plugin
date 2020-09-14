package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.ToolContentHoldable;
import org.performancetoolbox.intellij.plugin.common.actions.ToggleBooleanAction;
import org.performancetoolbox.intellij.plugin.common.bundles.Bundle;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static com.intellij.icons.AllIcons.Actions.Menu_saveall;
import static com.intellij.icons.AllIcons.Actions.PreviewDetails;
import static com.intellij.icons.AllIcons.Actions.ProfileMemory;
import static com.intellij.icons.AllIcons.Actions.Show;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;
import static org.performancetoolbox.intellij.plugin.histogram.ExporterFactory.supportedExtensions;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.INSTANCES;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.SIZE;

public class ToolContentHolder implements ToolContentHoldable {

    private final State state;
    private final String displayName;

    public ToolContentHolder(State state, String displayName) {
        this.displayName = displayName;
        this.state = state;
    }

    @Override
    public Icon getIcon() {
        return ProfileMemory;
    }

    @Override
    public JComponent getComponent() {
        final HistogramTableModel histogramTableModel = new HistogramTableModel(state);
        // create sortable table and sort by the last column in a descending order
        final JBTable table = new JBTable(histogramTableModel) {
            @Override
            protected @NotNull JTableHeader createDefaultTableHeader() {
                return new JBTableHeader() {
                    @Override
                    public String getToolTipText(@NotNull MouseEvent e) {
                        final int i = columnAtPoint(e.getPoint());
                        final int infoIndex = i >= 0 ? convertColumnIndexToModel(i) : -1;
                        return histogramTableModel.getColumnToolTip(infoIndex);
                    }
                };
            }
        };
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(histogramTableModel.getColumnCount() - 1);
        table.getRowSorter().toggleSortOrder(histogramTableModel.getColumnCount() - 1);

        final Bundle resourceBundle = getResourceBundle();
        final DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
        defaultActionGroup.add(new AnAction(getString("action.histogram.export.text"), getString("action.histogram.export.description"), Menu_saveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                final FileSaverDescriptor descriptor = new FileSaverDescriptor(getString("action.histogram.export.text"), getString("action.histogram.export.description"), supportedExtensions());
                final FileSaverDialog dialog = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, (Project) null);
                // Append extension manually to file name on MacOS because FileSaverDialog does not do it automatically.
                final String fileName = getDisplayName() + (SystemInfo.isMac ? "." + supportedExtensions()[0] : "");
                final VirtualFileWrapper virtualFileWrapper = dialog.save((VirtualFile) null, fileName);

                if (virtualFileWrapper != null) {
                    try {
                        ExporterFactory.getInstance(new LightVirtualFile(virtualFileWrapper.getFile().getPath())).export(table);
                    } catch (IOException ee) {/*ignored*/}
                }
            }
        });
        defaultActionGroup.add(new ToggleBooleanAction(getString("action.histogram.showType.text"), getString("action.histogram.showType.description"), PreviewDetails, false) {
            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                super.setSelected(e, state);

                if (state) {
                    histogramTableModel.setShowType(INSTANCES);
                } else {
                    histogramTableModel.setShowType(SIZE);
                }
            }
        });

        final DefaultActionGroup filterActionGroup = new DefaultActionGroup(getResourceBundle().getString("action.histogram.filter.text"), true);
        filterActionGroup.getTemplatePresentation().setIcon(Show);
        filterActionGroup.getTemplatePresentation().setDescription(getResourceBundle().getString("action.histogram.filter.description"));
        filterActionGroup.add(new ToggleBooleanAction(getString("action.histogram.filterTotal.text"), getString("action.histogram.filterTotal.description"), true) {
            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                super.setSelected(e, state);
                histogramTableModel.setShowTotal(state);
            }
        });
        filterActionGroup.add(new ToggleBooleanAction(getString("action.histogram.filterUnchanged.text"), getString("action.histogram.filterUnchanged.description"), true) {
            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                super.setSelected(e, state);
                histogramTableModel.setShowUnchanged(state);
            }
        });
        defaultActionGroup.add(filterActionGroup);

        final ActionToolbar actionBar = ActionManager.getInstance().createActionToolbar("histogram", defaultActionGroup, false);
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(new JBScrollPane(table), CENTER);
        jPanel.add(actionBar.getComponent(), WEST);

        return jPanel;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void dispose() {
    }
}