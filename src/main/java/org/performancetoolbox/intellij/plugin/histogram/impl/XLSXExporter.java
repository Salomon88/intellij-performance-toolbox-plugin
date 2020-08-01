package org.performancetoolbox.intellij.plugin.histogram.impl;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class XLSXExporter extends POIExporter {

    public XLSXExporter(VirtualFile virtualFile) {
        super(virtualFile);
    }

    @Override
    public void export(JBTable table) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            export(wb, table);
        }
    }
}