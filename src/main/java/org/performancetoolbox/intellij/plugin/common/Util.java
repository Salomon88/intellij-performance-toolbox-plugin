package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import com.tagtraum.perf.gcviewer.view.GCDocument;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static javax.swing.SwingWorker.StateValue.DONE;

public class Util {

    public static GCResource createGCResource(String... files) {
        if (files == null || files.length == 0) {
            return null;
        } else if (files.length == 1) {
            return new GcResourceFile(files[0]);
        }

        return new GcResourceSeries(stream(files).sorted(reverseOrder()).map(GcResourceFile::new).collect(toList()));
    }

    public static GCResource createGCResource(VirtualFile... files) {
        if (files == null || files.length == 0) {
            return null;
        }

        return createGCResource(stream(files).map(VirtualFile::getPath).toArray(String[]::new));
    }

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("gcviewerBundle");
    }

    public static String getNormalizedName(GCDocument gcDocument) {
        return getNormalizedName(gcDocument.getGCResources().get(0));
    }

    public static String getNormalizedName(GCResource gcResource) {
        String name = gcResource.getResourceName().replaceAll("\\\\", "/");

        if (name.contains("/")) {
            return name.substring(name.lastIndexOf("/") + 1);
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

    public static void doInBackground(Project project, ToolContentDataLoaderGroupTracker tracker, Runnable successAction) {
        final ResourceBundle resourceBundle = Util.getResourceBundle();
        ApplicationManager.getApplication().invokeLater(() -> ProgressManager.getInstance().run(new Task.Backgroundable(project, format(resourceBundle.getString("parsing.text"), tracker.getName()), true) {
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