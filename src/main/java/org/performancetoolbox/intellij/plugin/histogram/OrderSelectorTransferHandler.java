package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.ui.TableUtil;

import javax.activation.DataHandler;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import static java.awt.Cursor.DEFAULT_CURSOR;
import static java.awt.Cursor.getPredefinedCursor;
import static java.awt.dnd.DragSource.DefaultMoveDrop;
import static java.awt.dnd.DragSource.DefaultMoveNoDrop;

class OrderSelectorTransferHandler extends TransferHandler {

    private final DataFlavor localObjectFlavor = new DataFlavor(Integer.class, "Integer Row Index");
    private final JTable table;

    public OrderSelectorTransferHandler(JTable table) {
        this.table = table;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        return new DataHandler(table.getSelectedRow(), localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferSupport info) {
        boolean b = info.getComponent() == table && info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
        table.setCursor(b ? DefaultMoveDrop : DefaultMoveNoDrop);
        return b;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferSupport info) {
        JTable target = (JTable) info.getComponent();
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int index = dl.getRow();
        int max = table.getModel().getRowCount();

        if (index < 0 || index > max) {
            index = max;
        }

        target.setCursor(getPredefinedCursor(DEFAULT_CURSOR));

        try {
            int rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);

            if (rowFrom != -1 && rowFrom != index) {
                int[] rows = table.getSelectedRows();

                if (index < rowFrom) {
                    for (int i = 0; i < rowFrom - index; i++) {
                        TableUtil.moveSelectedItemsUp(table);
                    }
                } else if (index > rowFrom + rows.length) {
                    for (int i = 0; i < index - rowFrom - rows.length; i++) {
                        TableUtil.moveSelectedItemsDown(table);
                    }
                }

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        if (act == MOVE) {
            table.setCursor(getPredefinedCursor(DEFAULT_CURSOR));
        }
    }
}