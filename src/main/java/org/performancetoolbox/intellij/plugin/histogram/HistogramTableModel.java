package org.performancetoolbox.intellij.plugin.histogram;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.INSTANCES;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.SIZE;

public class HistogramTableModel extends AbstractTableModel {

    enum SHOW_TYPE {
        INSTANCES,
        SIZE
    }

    private List<State> filteredStates;
    private List<State> states;
    private SHOW_TYPE showType = SIZE;
    private boolean showTotal = true;
    private boolean showUnchanged = true;

    public HistogramTableModel(List<State> states) {
        this.states = states;
        filterStates();
    }

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
                return noDifferences() || columnIndex == (filteredStates.get(0).getDifferencesSizes().length + 3)
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
                return filteredStates.get(rowIndex).getName();
            case 1:
                return filteredStates.get(rowIndex).getModule();
            case 2:
                return showType == INSTANCES
                        ? filteredStates.get(rowIndex).getInitialInstances()
                        : filteredStates.get(rowIndex).getInitialSize();
            default:
                return noDifferences() || columnIndex == (filteredStates.get(rowIndex).getDifferencesSizes().length + 3)
                        ? (showType == INSTANCES ? filteredStates.get(rowIndex).getFinalInstances() : filteredStates.get(rowIndex).getFinalSize())
                        : (showType == INSTANCES ? filteredStates.get(rowIndex).getDifferencesInstances()[columnIndex - 3] : filteredStates.get(rowIndex).getDifferencesSizes()[columnIndex - 3]);
        }
    }

    @Override
    public int getColumnCount() {
        return 4 + (noDifferences() ? 0 : filteredStates.get(0).getDifferencesSizes().length);
    }

    @Override
    public int getRowCount() {
        return filteredStates.size();
    }

    public void setShowTotal(boolean showTotal) {
        if (this.showTotal != showTotal) {
            this.showTotal = showTotal;
            filterStates();
            fireTableDataChanged();
        }
    }

    public void setShowType(SHOW_TYPE showType) {
        if (this.showType != showType && showType != null) {
            this.showType = showType;
            filterStates();
            fireTableDataChanged();
        }
    }

    public void setShowUnchanged(boolean showUnchanged) {
        if (this.showUnchanged != showUnchanged) {
            this.showUnchanged = showUnchanged;
            filterStates();
            fireTableDataChanged();
        }
    }

    public void setStates(List<State> states) {
        this.states = states;
        filterStates();
        fireTableDataChanged();
    }

    private boolean noDifferences() {
        return filteredStates.size() == 0
                || filteredStates.get(0).getDifferencesSizes() == null
                || filteredStates.get(0).getDifferencesSizes().length == 0;
    }

    private void filterStates() {
        filteredStates = states.stream()
                .filter(state -> showTotal || !getResourceBundle().getString("table.histogram.total.name").equals(state.getName()))
                .filter(state -> showUnchanged || (showType == INSTANCES
                        ? !Objects.equals(state.getInitialInstances(), state.getFinalInstances())
                        : !Objects.equals(state.getInitialSize(), state.getFinalSize())))
                .collect(toList());
    }
}