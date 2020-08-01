package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.table.JBTable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.performancetoolbox.intellij.plugin.histogram.impl.CSVExporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class CSVExporterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void saveSingleSizeLastColumnSortedAsc() throws IOException {
        final File file = export(false, false);
        assertArrayEquals(
                new String[]{
                        getResourceBundle().getString("table.histogram.className.header") + "," + getResourceBundle().getString("table.histogram.moduleName.header") + "," + getResourceBundle().getString("table.histogram.initial.header") + "," + getResourceBundle().getString("table.histogram.final.header"),
                        "[I,,1,1",
                        "Total,,10,10"},
                readAllLines(file.toPath()).toArray(new String[0]));
    }

    @Test
    public void saveSizeLastColumnSortedAsc() throws IOException {
        final File file = export(true, false);
        assertArrayEquals(
                new String[]{
                        getResourceBundle().getString("table.histogram.className.header") + "," + getResourceBundle().getString("table.histogram.moduleName.header") + "," + getResourceBundle().getString("table.histogram.initial.header") + "," + getResourceBundle().getString("table.histogram.diff.header") + " #1," + getResourceBundle().getString("table.histogram.final.header"),
                        "[I,,1,9,10",
                        "Total,,10,10,20"},
                readAllLines(file.toPath()).toArray(new String[0]));
    }

    @Test
    public void saveSizeLastColumnSortedDesc() throws IOException {
        final File file = export(true, true);
        assertArrayEquals(
                new String[]{
                        getResourceBundle().getString("table.histogram.className.header") + "," + getResourceBundle().getString("table.histogram.moduleName.header") + "," + getResourceBundle().getString("table.histogram.initial.header") + "," + getResourceBundle().getString("table.histogram.diff.header") + " #1," + getResourceBundle().getString("table.histogram.final.header"),
                        "Total,,10,10,20",
                        "[I,,1,9,10"},
                readAllLines(file.toPath()).toArray(new String[0]));
    }

    private File export(boolean diff, boolean sortDesc) throws IOException {
        final List<State> states = asList(
                new State()
                        .setDifferencesInstances(diff ? new Long[]{1L} : null)
                        .setDifferencesSizes(diff ? new Long[]{10L} : null)
                        .setFinalInstances(diff ? 2L : 1L)
                        .setFinalSize(diff ? 20L : 10L)
                        .setInitialInstances(1L)
                        .setInitialSize(10L)
                        .setName("Total"),
                new State()
                        .setDifferencesInstances(diff ? new Long[]{9L} : null)
                        .setDifferencesSizes(diff ? new Long[]{9L} : null)
                        .setFinalInstances(diff ? 10L : 1L)
                        .setFinalSize(diff ? 10L : 1L)
                        .setInitialInstances(1L)
                        .setInitialSize(1L)
                        .setName("[I"));
        final HistogramTableModel histogramTableModel = new HistogramTableModel(states);
        final JBTable table = new JBTable(histogramTableModel);
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(histogramTableModel.getColumnCount() - 1);

        if (sortDesc) {
            table.getRowSorter().toggleSortOrder(histogramTableModel.getColumnCount() - 1);
        }

        final File file = temporaryFolder.newFile("test.csv");
        final Exporter exporter = ExporterFactory.getInstance(new LightVirtualFile(file.getPath()));
        assertTrue(exporter instanceof CSVExporter);
        exporter.export(table);

        return file;
    }
}