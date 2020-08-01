package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.ui.table.JBTable;

import java.io.IOException;

public interface Exporter {
    void export(JBTable table) throws IOException;
}