package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoadable;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoaderGroupTracker;
import org.performancetoolbox.intellij.plugin.common.ToolContentHoldable;
import org.performancetoolbox.intellij.plugin.common.ToolContentLoadable;
import org.performancetoolbox.intellij.plugin.common.impl.ToolContentDataLoaderGroupTrackerImpl;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.function.BiConsumer;

import static org.performancetoolbox.intellij.plugin.common.Util.doInBackground;
import static org.performancetoolbox.intellij.plugin.common.Util.getNormalizedName;

public class ToolContentLoader implements ToolContentLoadable<List<VirtualFile>> {

    private final Project project;

    public ToolContentLoader(Project project) {
        this.project = project;
    }

    @Override
    public void load(List<VirtualFile> files, BiConsumer<Project, ToolContentHoldable> callback) {
        ToolContentDataLoaderGroupTracker<State> toolContentDataLoaderGroupTracker = new ToolContentDataLoaderGroupTrackerImpl<>(getNormalizedName(files.get(0).getName()));
        ToolContentDataLoadable<State> toolContentDataLoadable = new ToolContentDataLoader(files);
        toolContentDataLoaderGroupTracker.addModelLoader(toolContentDataLoadable);
        doInBackground(project, toolContentDataLoaderGroupTracker, () -> callback.accept(project, new ToolContentHolder(toolContentDataLoadable.getContentData(), getNormalizedName(files.get(0).getName()))));
    }
}