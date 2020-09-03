package org.performancetoolbox.intellij.plugin.common.factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.tagtraum.perf.gcviewer.model.GCResource;
import org.performancetoolbox.intellij.plugin.common.ToolContentLoadable;
import org.performancetoolbox.intellij.plugin.gcviewer.ToolContentLoaderViewer;
import org.performancetoolbox.intellij.plugin.histogram.ToolContentLoaderHist;

import java.util.List;

public enum ToolContentLoadableFactory {

    CONTENT_FACTORY;

    public ToolContentLoadable<GCResource> getViewerInstance(Project project) {
        return new ToolContentLoaderViewer(project);
    }

    public ToolContentLoadable<List<VirtualFile>> getHistInstance(Project project) {
        return new ToolContentLoaderHist(project);
    }
}
