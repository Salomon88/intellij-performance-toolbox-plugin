package org.performancetoolbox.intellij.plugin.histogram;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.INSTANCES;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.SIZE;

public class HistogramTableModel extends AbstractTableModel {

    enum SHOW_TYPE {
        INSTANCES,
        SIZE
    }

    private List<State> states = new ArrayList<>();
    private SHOW_TYPE showType = SIZE;

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return getResourceBundle().getString("table.histogram.className.header");
            case 1:
                return getResourceBundle().getString("table.histogram.moduleName.header");
            case 2:
                return getResourceBundle().getString("table.histogram.initial.header");
            default:
                return states.size() == 0 || columnIndex == (states.get(0).getDifferencesSizes().length + 3)
                        ? getResourceBundle().getString("table.histogram.final.header")
                        : getResourceBundle().getString("table.histogram.diff.header") + " #" + (columnIndex - 2);
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
                return showType == INSTANCES
                        ? states.get(rowIndex).getInitialInstances()
                        : states.get(rowIndex).getInitialSize();
            default:
                return states.size() == 0 || columnIndex == (states.get(rowIndex).getDifferencesSizes().length + 3)
                        ? (showType == INSTANCES ? states.get(rowIndex).getFinalInstances() : states.get(rowIndex).getFinalSize())
                        : (showType == INSTANCES ? states.get(rowIndex).getDifferencesInstances()[columnIndex - 3] : states.get(rowIndex).getDifferencesSizes()[columnIndex - 3]);
        }
    }

    @Override
    public int getColumnCount() {
        return 4 + (states.size() == 0 ? 0 : states.get(0).getDifferencesSizes().length);
    }

    @Override
    public int getRowCount() {
        return states.size();
    }

    public void setShowType(SHOW_TYPE showType) {
        if (this.showType != showType && showType != null) {
            this.showType = showType;
            fireTableDataChanged();
        }
    }

    public void setStates(List<State> states) {
        this.states = states;
        fireTableDataChanged();
    }
}