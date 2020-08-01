package org.performancetoolbox.intellij.plugin.histogram.impl;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

public class XLSExporter extends POIExporter {

    public XLSExporter(VirtualFile virtualFile) {
        super(virtualFile);
    }

    @Override
    public void export(JBTable table) throws IOException {
        try (Workbook wb = new HSSFWorkbook()) {
            export(wb, table);
        }
    }
}