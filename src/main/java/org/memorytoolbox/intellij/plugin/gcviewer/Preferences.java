package org.memorytoolbox.intellij.plugin.gcviewer;

import com.tagtraum.perf.gcviewer.view.model.GCPreferences;

public class Preferences extends GCPreferences {

    public Preferences(PreferencesComponent preferencesComponent) {
        setBooleanProperty(ANTI_ALIAS, preferencesComponent.isAntiAlias());
        setBooleanProperty(CONCURRENT_COLLECTION_BEGIN_END, preferencesComponent.isConcurrentCollectionBeginEnd());
        setBooleanProperty(FULL_GC_LINES, preferencesComponent.isFullGcLines());
        setBooleanProperty(INC_GC_LINES, preferencesComponent.isGcTimesLine());
        setBooleanProperty(INITIAL_MARK_LEVEL, preferencesComponent.isInitialMarkLevel());
        setBooleanProperty(GC_TIMES_LINE, preferencesComponent.isGcTimesLine());
        setBooleanProperty(GC_TIMES_RECTANGLES, preferencesComponent.isGcTimesRectangles());
        setBooleanProperty(SHOW_DATE_STAMP, preferencesComponent.isShowDatestamp());
        setBooleanProperty(TENURED_MEMORY, preferencesComponent.isTenuredMemory());
        setBooleanProperty(TOTAL_MEMORY, preferencesComponent.isTotalMemory());
        setBooleanProperty(USED_MEMORY, preferencesComponent.isUsedMemory());
        setBooleanProperty(USED_TENURED_MEMORY, preferencesComponent.isUsedTenuredMemory());
        setBooleanProperty(USED_YOUNG_MEMORY, preferencesComponent.isUsedYoungMemory());
    }

    @Override
    public boolean isPropertiesLoaded() {
        return true;
    }

    @Override
    public void load() {
    }

    @Override
    public void store() {
    }
}
