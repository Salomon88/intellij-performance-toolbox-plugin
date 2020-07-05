package com.github.gcviewerplugin;

import com.tagtraum.perf.gcviewer.view.GCDocument;
import com.tagtraum.perf.gcviewer.view.GCViewerGui;

public class MockedGCViewerGui extends GCViewerGui {

    @Override
    public GCDocument getSelectedGCDocument() {
        return null;
    }
}