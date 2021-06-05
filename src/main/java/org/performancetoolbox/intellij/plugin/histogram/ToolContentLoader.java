package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoadable;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoaderGroupTracker;
import org.performancetoolbox.intellij.plugin.common.ToolContentHoldable;
import org.performancetoolbox.intellij.plugin.common.ToolContentLoadable;
import org.performancetoolbox.intellij.plugin.common.impl.ToolContentDataLoaderGroupTrackerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.intellij.openapi.components.ServiceManager.getService;
import static com.intellij.openapi.util.io.FileUtil.toSystemDependentName;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.performancetoolbox.intellij.plugin.common.Util.doInBackground;
import static org.performancetoolbox.intellij.plugin.common.Util.getHistoryRecord;
import static org.performancetoolbox.intellij.plugin.common.Util.getNormalizedName;

public class ToolContentLoader implements ToolContentLoadable<List<VirtualFile>> {

    private final Project project;

    public ToolContentLoader(Project project) {
        this.project = project;
    }

    @Override
    public void load(List<VirtualFile> files, BiConsumer<Project, ToolContentHoldable> callback) {
        List<VirtualFile> orderedFiles = ordered(files);
        ToolContentDataLoaderGroupTracker<State> toolContentDataLoaderGroupTracker = new ToolContentDataLoaderGroupTrackerImpl<>(getNormalizedName(files.get(0).getName()));
        ToolContentDataLoadable<State> toolContentDataLoadable = new ToolContentDataLoader(orderedFiles);
        toolContentDataLoaderGroupTracker.addModelLoader(toolContentDataLoadable);
        doInBackground(project, toolContentDataLoaderGroupTracker, () -> callback.accept(project, new ToolContentHolder(toolContentDataLoadable.getContentData(), getNormalizedName(files.get(0).getName()))));
    }

    public void reload(List<VirtualFile> orderedFiles, Consumer<ToolContentDataLoadable<State>> callback) {
        List<VirtualFile> files = sorted(orderedFiles);
        ToolContentDataLoaderGroupTracker<State> toolContentDataLoaderGroupTracker = new ToolContentDataLoaderGroupTrackerImpl<>(getNormalizedName(files.get(0).getName()));
        ToolContentDataLoadable<State> toolContentDataLoadable = new ToolContentDataLoader(orderedFiles);
        toolContentDataLoaderGroupTracker.addModelLoader(toolContentDataLoadable);
        doInBackground(project, toolContentDataLoaderGroupTracker, () -> callback.accept(toolContentDataLoadable));
    }

    private List<VirtualFile> ordered(List<VirtualFile> files) {
        String filePath = getHistoryRecord(files);
        PreferenceData preferencesData = getService(PreferencesComponent.class).getPreferenceData(filePath);

        if (preferencesData.getOrder() == null || preferencesData.getOrder().size() != files.size()) {
            preferencesData.setOrder(range(0, files.size()).boxed().collect(toList()));
        }

        List<VirtualFile> orderedFiles = new ArrayList<>(files.size());

        for (int index : preferencesData.getOrder()) {
            orderedFiles.add(files.get(index));
        }

        return orderedFiles;
    }

    private List<VirtualFile> sorted(List<VirtualFile> orderedFiles) {
        List<VirtualFile> files = orderedFiles.stream().sorted(comparing(o -> toSystemDependentName(o.getPath()))).collect(toList());
        String filePath = getHistoryRecord(files);
        PreferenceData preferencesData = getService(PreferencesComponent.class).getPreferenceData(filePath);
        preferencesData.setOrder(new ArrayList<>());

        for (VirtualFile file : orderedFiles) {
            preferencesData.getOrder().add(files.indexOf(file));
        }

        return files;
    }
}