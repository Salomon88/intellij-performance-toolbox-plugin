package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class OrderSelectorTableModel extends AbstractTableModel {

    private final List<VirtualFile> files;
    private final Map<String, VirtualFile> fileMap;

    public OrderSelectorTableModel(List<VirtualFile> files) {
        this.files = new ArrayList<>(files);
        this.fileMap = files.stream().collect(Collectors.toMap(VirtualFile::getName, Function.identity()));
    }

    public List<VirtualFile> getFiles() {
        return files;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return getResourceBundle().getString("histogram.orderSelector.table.order.header");
            case 1:
                return getResourceBundle().getString("histogram.orderSelector.table.fileName.header");
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getColumnToolTip(int columnIndex) {
        if (columnIndex == 0) {
            return getResourceBundle().getString("histogram.orderSelector.table.order.tooltip");
        } else {
            return getResourceBundle().getString("histogram.orderSelector.table.fileName.tooltip");
        }
    }

    @Override
    public int getRowCount() {
        return files.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex;
        } else {
            return files.get(rowIndex).getName();
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            files.set(rowIndex, fileMap.get((String) aValue));
        }
    }
}