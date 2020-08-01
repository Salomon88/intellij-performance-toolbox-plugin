package org.performancetoolbox.intellij.plugin.histogram.impl;

import com.intellij.openapi.vfs.VirtualFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.performancetoolbox.intellij.plugin.histogram.Exporter;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static java.util.Optional.ofNullable;

public abstract class POIExporter implements Exporter {

    private static final String SHEET_NAME = "Data";
    private final VirtualFile virtualFile;

    public POIExporter(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    protected void export(Workbook wb, JTable table) throws IOException {
        CreationHelper createHelper = wb.getCreationHelper();

        try (OutputStream fileOut = new FileOutputStream(virtualFile.getPath())) {
            Sheet sheet = wb.createSheet(SHEET_NAME);
            sheet.setAutoFilter(new CellRangeAddress(0, table.getRowCount(), 0, table.getColumnCount() - 1));
            Row row = sheet.createRow(0);

            for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
                Cell cell = row.createCell(columnIndex);

                ofNullable(table.getColumnName(columnIndex)).ifPresent(name -> cell.setCellValue(createHelper.createRichTextString(name)));
            }

            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                int rawRowIndex = table.getRowSorter().convertRowIndexToModel(rowIndex);
                row = sheet.createRow(1 + rowIndex);

                for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
                    Cell cell = row.createCell(columnIndex);

                    ofNullable(table.getModel().getValueAt(rawRowIndex, columnIndex)).ifPresent(value -> {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else {
                            cell.setCellValue((Long) value);
                        }
                    });
                }
            }

            wb.write(fileOut);
        }
    }
}
