package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.ToolContentHoldable;
import org.performancetoolbox.intellij.plugin.gcviewer.actions.ToggleBooleanAction;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.intellij.icons.AllIcons.Actions.PreviewDetails;
import static com.intellij.icons.AllIcons.Actions.ProfileMemory;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import static java.util.stream.Collectors.toList;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.INSTANCES;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.SIZE;

public class ToolContentHolder implements ToolContentHoldable {

    private List<State> states;
    private String displayName;

    public ToolContentHolder(List<State> states, String displayName) {
        this.displayName = displayName;
        this.states = states;
    }

    @Override
    public Icon getIcon() {
        return ProfileMemory;
    }

    @Override
    public JComponent getComponent() {
        final HistogramTableModel histogramTableModel = new HistogramTableModel();
        histogramTableModel.setStates(states);
        JBTable table = new JBTable(histogramTableModel);
        table.setAutoCreateRowSorter(true);

        final ResourceBundle resourceBundle = getResourceBundle();
        final DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
        defaultActionGroup.add(new ToggleBooleanAction(resourceBundle.getString("action.histogram.filter.text"), resourceBundle.getString("action.histogram.filter.description"), PreviewDetails, false) {
            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                super.setSelected(e, state);

                if (state) {
                    List<State> filteredStates = states.stream().filter(s -> !Objects.equals(s.getInitialSize(), s.getFinalSize())).collect(toList());
                    histogramTableModel.setStates(filteredStates);
                } else {
                    histogramTableModel.setStates(states);
                }
            }
        });
        defaultActionGroup.add(new ToggleBooleanAction(resourceBundle.getString("action.histogram.showType.text"), resourceBundle.getString("action.histogram.showType.description"), PreviewDetails, false) {
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