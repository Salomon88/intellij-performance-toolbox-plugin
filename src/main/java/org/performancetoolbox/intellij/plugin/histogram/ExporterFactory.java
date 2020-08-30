package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;
import org.performancetoolbox.intellij.plugin.histogram.impl.CSVExporter;
import org.performancetoolbox.intellij.plugin.histogram.impl.XLSExporter;
import org.performancetoolbox.intellij.plugin.histogram.impl.XLSXExporter;

import static java.util.Optional.ofNullable;

public class ExporterFactory {

    private ExporterFactory() {
    }

    public static Exporter getInstance(VirtualFile file) {
        switch (ofNullable(file.getExtension()).orElse("").toLowerCase()) {
            case "csv":
                return new CSVExporter(file);
            case "xls":
                return new XLSExporter(file);
            case "xlsx":
                return new XLSXExporter(file);
            default:
                throw new IllegalArgumentException("Unsupported export format");
        }
    }

    public static String[] supportedExtensions() {
        return new String[]{"csv", "xls", "xlsx"};
    }
}