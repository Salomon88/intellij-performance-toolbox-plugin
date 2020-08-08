package org.performancetoolbox.intellij.plugin.gcviewer;

import com.tagtraum.perf.gcviewer.view.model.GCPreferences;

public class PreferencesWrapper extends GCPreferences {

    /*
    This prefix related on inner logic of GC plugin, located in com.tagtraum.perf.gcviewer.view.model.GCPreferences
     */
    private static final String GC_LINE_PREFIX = "view.";

    public PreferencesWrapper(PreferenceData preferenceData) {
        setBooleanProperty(GC_LINE_PREFIX + ANTI_ALIAS, preferenceData.isAntiAlias());
        setBooleanProperty(GC_LINE_PREFIX + CONCURRENT_COLLECTION_BEGIN_END, preferenceData.isConcurrentCollectionBeginEnd());
        setBooleanProperty(GC_LINE_PREFIX + FULL_GC_LINES, preferenceData.isFullGcLines());
        setBooleanProperty(GC_LINE_PREFIX + INC_GC_LINES, preferenceData.isGcTimesLine());
        setBooleanProperty(GC_LINE_PREFIX + INITIAL_MARK_LEVEL, preferenceData.isInitialMarkLevel());
        setBooleanProperty(GC_LINE_PREFIX + GC_TIMES_LINE, preferenceData.isGcTimesLine());
        setBooleanProperty(GC_LINE_PREFIX + GC_TIMES_RECTANGLES, preferenceData.isGcTimesRectangles());
        setBooleanProperty(GC_LINE_PREFIX + SHOW_DATE_STAMP, preferenceData.isShowDatestamp());
        setBooleanProperty(GC_LINE_PREFIX + TENURED_MEMORY, preferenceData.isTenuredMemory());
        setBooleanProperty(GC_LINE_PREFIX + TOTAL_MEMORY, preferenceData.isTotalMemory());
        setBooleanProperty(GC_LINE_PREFIX + USED_MEMORY, preferenceData.isUsedMemory());
        setBooleanProperty(GC_LINE_PREFIX + USED_TENURED_MEMORY, preferenceData.isUsedTenuredMemory());
        setBooleanProperty(GC_LINE_PREFIX + USED_YOUNG_MEMORY, preferenceData.isUsedYoungMemory());
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
