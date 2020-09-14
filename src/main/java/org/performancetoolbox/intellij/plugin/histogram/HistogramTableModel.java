package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;
import org.performancetoolbox.intellij.plugin.histogram.State.ClassState;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.INSTANCES;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.SIZE;

public class HistogramTableModel extends AbstractTableModel {

    enum SHOW_TYPE {
        INSTANCES,
        SIZE
    }

    private List<ClassState> filteredClassStates;
    private SHOW_TYPE showType = SIZE;
    private final State state;
    private boolean showTotal = true;
    private boolean showUnchanged = true;

    public HistogramTableModel(State state) {
        this.state = state;
        filterClassStates();
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
                return filteredClassStates.get(rowIndex).getName();
            case 1:
                return filteredClassStates.get(rowIndex).getModule();
            case 2:
                return showType == INSTANCES
                        ? filteredClassStates.get(rowIndex).getInitialInstances()
                        : filteredClassStates.get(rowIndex).getInitialSize();
            default:
                return noDifferences() || columnIndex == (filteredClassStates.get(rowIndex).getDifferencesSizes().length + 3)
                        ? (showType == INSTANCES ? filteredClassStates.get(rowIndex).getFinalInstances() : filteredClassStates.get(rowIndex).getFinalSize())
                        : (showType == INSTANCES ? filteredClassStates.get(rowIndex).getDifferencesInstances()[columnIndex - 3] : filteredClassStates.get(rowIndex).getDifferencesSizes()[columnIndex - 3]);
        }
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
                return noDifferences() || columnIndex == (filteredClassStates.get(0).getDifferencesSizes().length + 3)
                        ? getResourceBundle().getString("table.histogram.final.header")
                        : getResourceBundle().getString("table.histogram.diff.header") + " #" + (columnIndex - 2);
        }
    }

    public String getColumnToolTip(int columnIndex) {
        if (columnIndex == 2) {
            return format(getString("table.histogram.initial.tooltip"), formatFileName(state.getFiles().get(0)));
        } else if (columnIndex < 2) {
            return null;
        } else if (noDifferences() || columnIndex == (filteredClassStates.get(0).getDifferencesSizes().length + 3)) {
            return format(getResourceBundle().getString("table.histogram.final.tooltip"), formatFileName(state.getFiles().get(state.getFiles().size() - 1)));
        } else {
            return format(getResourceBundle().getString("table.histogram.diff.tooltip"), formatFileName(state.getFiles().get(columnIndex - 3)), formatFileName(state.getFiles().get(columnIndex - 2)));
        }
    }

    @Override
    public int getColumnCount() {
        return 4 + (noDifferences() ? 0 : filteredClassStates.get(0).getDifferencesSizes().length);
    }

    @Override
    public int getRowCount() {
        return filteredClassStates.size();
    }

    public void setShowTotal(boolean showTotal) {
        if (this.showTotal != showTotal) {
            this.showTotal = showTotal;
            filterClassStates();
            fireTableDataChanged();
        }
    }

    public void setShowType(SHOW_TYPE showType) {
        if (this.showType != showType && showType != null) {
            this.showType = showType;
            filterClassStates();
            fireTableDataChanged();
        }
    }

    public void setShowUnchanged(boolean showUnchanged) {
        if (this.showUnchanged != showUnchanged) {
            this.showUnchanged = showUnchanged;
            filterClassStates();
            fireTableDataChanged();
        }
    }

    private String formatFileName(VirtualFile file) {
        return file.getName();
    }

    private boolean noDifferences() {
        return filteredClassStates.isEmpty()
                || filteredClassStates.get(0).getDifferencesSizes() == null
                || filteredClassStates.get(0).getDifferencesSizes().length == 0;
    }

    private void filterClassStates() {
        filteredClassStates = state.getClassStates().stream()
                .filter(state -> showTotal || !getString("table.histogram.total.name").equals(state.getName()))
                .filter(state -> showUnchanged || (showType == INSTANCES
                        ? !Objects.equals(state.getInitialInstances(), state.getFinalInstances())
                        : !Objects.equals(state.getInitialSize(), state.getFinalSize())))
                .collect(toList());
    }
}