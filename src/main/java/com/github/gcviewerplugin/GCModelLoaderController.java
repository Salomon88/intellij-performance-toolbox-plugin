package com.github.gcviewerplugin;

import com.intellij.openapi.progress.ProgressIndicator;
import com.tagtraum.perf.gcviewer.ctrl.GCModelLoader;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;

import java.beans.PropertyChangeListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.swing.SwingWorker.StateValue.DONE;

public class GCModelLoaderController {

    private GCModelLoader gcModelLoader;
    private ProgressIndicator indicator;
    private GCResource gcResource;
    private int seriesSize;

    public GCModelLoaderController(GCModelLoader gcModelLoader, GCResource gcResource, ProgressIndicator indicator) {
        this.gcModelLoader = gcModelLoader;
        this.gcResource = gcResource;
        this.indicator = indicator;
        this.seriesSize = gcResource instanceof GcResourceSeries ? ((GcResourceSeries) gcResource).getResourcesInOrder().size() : 1;
    }

    public void run() throws InterruptedException {
        final AtomicInteger accumulator = new AtomicInteger();
        final CountDownLatch latch = new CountDownLatch(1);
        final PropertyChangeListener propertyChangeListener = evt -> {
            if (indicator.isCanceled() || "state".equals(evt.getPropertyName()) && DONE == evt.getNewValue()) {
                latch.countDown();
            } else if ("progress".equals(evt.getPropertyName())) {
                if (seriesSize > 1) {
                    if ((Integer) evt.getNewValue() < (Integer) evt.getOldValue()) {
                        accumulator.addAndGet(100);
                    }
                }

                indicator.setFraction(((Integer) evt.getNewValue() + accumulator.get()) / 100. / seriesSize);
            }
        };

        try {
            gcModelLoader.addPropertyChangeListener(propertyChangeListener);
            gcModelLoader.execute();
            latch.await();
            indicator.checkCanceled();
        } finally {
            gcModelLoader.removePropertyChangeListener(propertyChangeListener);
            cancelLoading();
        }
    }

    private void cancelLoading() {
        if (gcResource instanceof GcResourceSeries) {
            for (GCResource resource : ((GcResourceSeries) gcResource).getResourcesInOrder()) {
                resource.setIsReadCancelled(true);
            }
        } else {
            gcResource.setIsReadCancelled(true);
        }
    }
}