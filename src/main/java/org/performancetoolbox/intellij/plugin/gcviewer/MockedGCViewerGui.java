package org.performancetoolbox.intellij.plugin.gcviewer;

import com.tagtraum.perf.gcviewer.view.GCDocument;
import com.tagtraum.perf.gcviewer.view.GCViewerGui;

public class MockedGCViewerGui extends GCViewerGui {

    private GCDocument gcDocument;

    public MockedGCViewerGui() {
        this(null);
    }

    public MockedGCViewerGui(GCDocument gcDocument) {
        this.gcDocument = gcDocument;
    }

    @Override
    public GCDocument getSelectedGCDocument() {
        return gcDocument;
    }
}