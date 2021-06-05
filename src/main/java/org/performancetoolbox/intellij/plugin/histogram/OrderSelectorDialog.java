package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.DialogWrapperWithResult;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.awt.BorderLayout.CENTER;
import static org.performancetoolbox.intellij.plugin.common.Util.PREFERRED_WIDTH;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class OrderSelectorDialog extends DialogWrapperWithResult<List<VirtualFile>> {

    private final List<VirtualFile> files;
    private final OrderSelectorTableModel orderSelectorTableModel;

    public OrderSelectorDialog(Project project, List<VirtualFile> files) {
        super(project);
        this.files = new ArrayList<>(files);
        this.orderSelectorTableModel = new OrderSelectorTableModel(files);
        init();
        setTitle(getResourceBundle().getString("histogram.orderSelector.title"));
    }

    @Override
    public List<VirtualFile> getResult() {
        return orderSelectorTableModel.getFiles();
    }

    @Override
    protected JComponent createCenterPanel() {
        final JBTable table = new JBTable(orderSelectorTableModel) {
            @Override
            protected @NotNull JTableHeader createDefaultTableHeader() {
                return new JBTable.JBTableHeader() {
                    @Override
                    public String getToolTipText(@NotNull MouseEvent e) {
                        final int i = columnAtPoint(e.getPoint());
                        final int infoIndex = i >= 0 ? convertColumnIndexToModel(i) : -1;
                        return orderSelectorTableModel.getColumnToolTip(infoIndex);
                    }
                };
            }
        };

        table.getColumnModel().getColumn(0).setMaxWidth(100);

        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new OrderSelectorTransferHandler(table));

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), CENTER);
        panel.setMinimumSize(new Dimension(PREFERRED_WIDTH, 0));
        return panel;
    }

    @Override
    protected void doOKAction() {
        if (Objects.equals(files, orderSelectorTableModel.getFiles())) {
            doCancelAction();
        } else {
            super.doOKAction();
        }
    }
}