package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.common.bundles.Bundle;
import org.reflections.Reflections;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static javax.swing.SwingWorker.StateValue.DONE;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public class Util {

    private Util() {
    }

    public static Optional<GCResource> createGCResource(List<VirtualFile> files) {
        if (files == null || files.isEmpty()) {
            return empty();
        }

        return createGCResource(files.stream().map(VirtualFile::getPath).toArray(String[]::new));
    }

    public static Optional<GCResource> createGCResource(String... files) {
        if (isNull(files) || files.length == 0) {
            return empty();
        } else if (files.length == 1) {
            return of(new GcResourceFile(files[0]));
        }

        return of(new GcResourceSeries(stream(files).sorted(reverseOrder()).map(GcResourceFile::new).collect(toList())));
    }

    public static Optional<GCResource> createGCResource(VirtualFile... files) {
        if (files == null || files.length == 0) {
            return empty();
        }

        return createGCResource(stream(files).map(VirtualFile::getPath).toArray(String[]::new));
    }

    public static List<VirtualFile> getUnpackedHistoryRecord(String historyRecord) {
        return stream(historyRecord.split(";")).map(LightVirtualFile::new).collect(toList());
    }

    public static Bundle getResourceBundle() {
        return Bundle.INSTANCE;
    }

    public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        Reflections reflections = new Reflections("org.performancetoolbox.intellij.plugin");
        return reflections.getSubTypesOf(type);
    }

    public static String getHistoryRecord(List<VirtualFile> files) {
        return files.stream()
                .map(VirtualFile::getPath)
                .map(FileUtil::toSystemDependentName)
                .sorted()
                .collect(Collectors.joining(";"));
    }

    public static String getNormalizedName(GCDocument gcDocument) {
        return getNormalizedName(gcDocument.getGCResources().get(0));
    }

    public static String getNormalizedName(GCResource gcResource) {
        return getNormalizedName(gcResource.getResourceName());
    }

    public static String getNormalizedName(String name) {
        name = name.replaceAll("\\\\", "/");

        if (name.contains("/")) {
            return name.substring(name.lastIndexOf('/') + 1);
        }

        return name;
    }

    public static <T extends PropertyChangeListener> T getPropertyChangeListener(GCDocument document, Class<T> clazz) {
        for (PropertyChangeListener listener : document.getPropertyChangeListeners()) {
            if (clazz.isInstance(listener)) {
                return clazz.cast(listener);
            }
        }

        throw new IllegalStateException("Failed to find listener of class " + clazz);
    }

    public static void doInBackground(Project project, ToolContentDataLoaderGroupTracker<?> tracker, Runnable successAction) {
        ApplicationManager.getApplication().invokeLater(() -> ProgressManager.getInstance().run(new Task.Backgroundable(project, format(getString("parsing.text"), tracker.getName()), true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                final CountDownLatch latch = new CountDownLatch(1);
                final PropertyChangeListener propertyChangeListener = evt -> {
                    if (indicator.isCanceled() || "state".equals(evt.getPropertyName()) && DONE == evt.getNewValue()) {
                        latch.countDown();
                    } else if ("progress".equals(evt.getPropertyName())) {
                        indicator.setFraction((Integer) evt.getNewValue() / 100.);
                    }
                };

                try {
                    tracker.addPropertyChangeListener(propertyChangeListener);
                    tracker.execute();
                    latch.await();
                    indicator.checkCanceled();

                    if (successAction != null) {
                        successAction.run();
                    }
                } catch (Exception e) {
                    tracker.cancel();
                } finally {
                    tracker.removePropertyChangeListener(propertyChangeListener);
                }
            }
        }));
    }
}