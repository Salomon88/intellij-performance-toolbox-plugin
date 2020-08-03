package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.table.JBTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.INSTANCES;
import static org.performancetoolbox.intellij.plugin.histogram.HistogramTableModel.SHOW_TYPE.SIZE;

public class ExportersTest {

    @TempDir
    public Path tempDir;

    @Test
    public void csvInstancesSizeLastColumnSortedAsc() throws IOException {
        final File file = export("test.csv", INSTANCES, false, false);
        assertArrayEquals(
                new String[]{
                        getResourceBundle().getString("table.histogram.className.header") + "," + getResourceBundle().getString("table.histogram.moduleName.header") + "," + getResourceBundle().getString("table.histogram.initial.header") + "," + getResourceBundle().getString("table.histogram.final.header"),
                        "Total,,1,1",
                        "[I,,2,2"},
                readAllLines(file.toPath()).toArray(new String[0]));
    }

    @Test
    public void csvSingleSizeLastColumnSortedAsc() throws IOException {
        final File file = export("test.csv", SIZE, false, false);
        assertArrayEquals(
                new String[]{
                        getResourceBundle().getString("table.histogram.className.header") + "," + getResourceBundle().getString("table.histogram.moduleName.header") + "," + getResourceBundle().getString("table.histogram.initial.header") + "," + getResourceBundle().getString("table.histogram.final.header"),
                        "[I,,1,1",
                        "Total,,10,10"},
                readAllLines(file.toPath()).toArray(new String[0]));
    }

    @Test
    public void csvSizeLastColumnSortedAsc() throws IOException {
        final File file = export("test.csv", SIZE, true, false);
        assertArrayEquals(
                new String[]{
                        getResourceBundle().getString("table.histogram.className.header") + "," + getResourceBundle().getString("table.histogram.moduleName.header") + "," + getResourceBundle().getString("table.histogram.initial.header") + "," + getResourceBundle().getString("table.histogram.diff.header") + " #1," + getResourceBundle().getString("table.histogram.final.header"),
                        "[I,,1,9,10",
                        "Total,,10,10,20"},
                readAllLines(file.toPath()).toArray(new String[0]));
    }

    @Test
    public void csvSizeLastColumnSortedDesc() throws IOException {
        final File file = export("test.csv", SIZE, true, true);
        assertArrayEquals(
                new String[]{
                        getResourceBundle().getString("table.histogram.className.header") + "," + getResourceBundle().getString("table.histogram.moduleName.header") + "," + getResourceBundle().getString("table.histogram.initial.header") + "," + getResourceBundle().getString("table.histogram.diff.header") + " #1," + getResourceBundle().getString("table.histogram.final.header"),
                        "Total,,10,10,20",
                        "[I,,1,9,10"},
                readAllLines(file.toPath()).toArray(new String[0]));
    }

    @Test
    public void xlsSizeLastColumnSortedDesc() throws IOException {
        final File file = export("test.xls", SIZE, true, true);
        assertTrue(file.exists());
    }

    @Test
    public void xlsxSizeLastColumnSortedDesc() throws IOException {
        final File file = export("test.xlsx", SIZE, true, true);
        assertTrue(file.exists());
    }

    private File export(String fileName, SHOW_TYPE showType, boolean diff, boolean sortDesc) throws IOException {
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
                        .setFinalInstances(diff ? 10L : 2L)
                        .setFinalSize(diff ? 10L : 1L)
                        .setInitialInstances(2L)
                        .setInitialSize(1L)
                        .setName("[I"));
        final HistogramTableModel histogramTableModel = new HistogramTableModel(states);
        histogramTableModel.setShowType(showType);
        final JBTable table = new JBTable(histogramTableModel);
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(histogramTableModel.getColumnCount() - 1);

        if (sortDesc) {
            table.getRowSorter().toggleSortOrder(histogramTableModel.getColumnCount() - 1);
        }

        final File file = Paths.get(tempDir.toString(), fileName).toFile();
        final Exporter exporter = ExporterFactory.getInstance(new LightVirtualFile(file.getPath()));
        exporter.export(table);

        return file;
    }
}