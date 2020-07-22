package org.performancetoolbox.intellij.plugin.histogram;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class HistogramTableModel extends AbstractTableModel {
    private List<State> states = new ArrayList<>();

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return getResourceBundle().getString("table.histogram.className.header");
            case 1:
                return getResourceBundle().getString("table.histogram.moduleName.header");
            case 2:
                return getResourceBundle().getString("table.histogram.initialSize.header");
            default:
                return states.size() == 0 || columnIndex == (states.get(0).getDifferences().length + 3)
                        ? getResourceBundle().getString("table.histogram.finalSize.header")
                        : getResourceBundle().getString("table.histogram.diff.header") + " #" + (columnIndex - 3);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return String.class;
            default:
                return Long.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return states.get(rowIndex).getName();
            case 1:
                return states.get(rowIndex).getModule();
            case 2:
                return states.get(rowIndex).getInitialSize();
            default:
                return states.size() == 0 || columnIndex == (states.get(rowIndex).getDifferences().length + 3)
                        ? states.get(rowIndex).getFinalSize()
                        : states.get(rowIndex).getDifferences()[columnIndex - 3];
        }
    }

    @Override
    public int getColumnCount() {
        return 4 + (states.size() == 0 ? 0 : states.get(0).getDifferences().length);
    }

    @Override
    public int getRowCount() {
        return states.size();
    }

    public void setStates(List<State> states) {
        this.states = states;
        fireTableDataChanged();
    }
}