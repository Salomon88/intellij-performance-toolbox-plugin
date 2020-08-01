package org.performancetoolbox.intellij.plugin.histogram.impl;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import org.performancetoolbox.intellij.plugin.histogram.Exporter;

import java.io.IOException;
import java.io.PrintWriter;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

public class CSVExporter implements Exporter {

    private static final String SEPARATOR = ",";
    private final VirtualFile file;

    public CSVExporter(VirtualFile file) {
        this.file = file;
    }

    @Override
    public void export(JBTable table) throws IOException {
        try (PrintWriter pw = new PrintWriter(file.getPath())) {
            pw.println(range(0, table.getColumnCount())
                    .mapToObj(table::getColumnName)
                    .map(name -> name == null ? "" : name)
                    .collect(joining(SEPARATOR)));

            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                int rawRowIndex = table.getRowSorter().convertRowIndexToModel(rowIndex);
                pw.println(range(0, table.getColumnCount())
                        .mapToObj(columnIndex -> table.getModel().getValueAt(rawRowIndex, columnIndex))
                        .map(value -> value == null ? "" : value.toString())
                        .collect(joining(SEPARATOR)));
            }
        }
    }
}