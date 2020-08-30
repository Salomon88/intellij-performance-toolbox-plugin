package org.performancetoolbox.intellij.plugin.gcviewer;

import com.tagtraum.perf.gcviewer.view.model.GCPreferences;

public class PreferencesWrapper extends GCPreferences {

    public PreferencesWrapper(PreferenceData preferenceData) {
        setGcLineProperty(ANTI_ALIAS, preferenceData.isAntiAlias());
        setGcLineProperty(CONCURRENT_COLLECTION_BEGIN_END, preferenceData.isConcurrentCollectionBeginEnd());
        setGcLineProperty(FULL_GC_LINES, preferenceData.isFullGcLines());
        setGcLineProperty(INC_GC_LINES, preferenceData.isGcTimesLine());
        setGcLineProperty(INITIAL_MARK_LEVEL, preferenceData.isInitialMarkLevel());
        setGcLineProperty(GC_TIMES_LINE, preferenceData.isGcTimesLine());
        setGcLineProperty(GC_TIMES_RECTANGLES, preferenceData.isGcTimesRectangles());
        setGcLineProperty(SHOW_DATE_STAMP, preferenceData.isShowDatestamp());
        setGcLineProperty(TENURED_MEMORY, preferenceData.isTenuredMemory());
        setGcLineProperty(TOTAL_MEMORY, preferenceData.isTotalMemory());
        setGcLineProperty(USED_MEMORY, preferenceData.isUsedMemory());
        setGcLineProperty(USED_TENURED_MEMORY, preferenceData.isUsedTenuredMemory());
        setGcLineProperty(USED_YOUNG_MEMORY, preferenceData.isUsedYoungMemory());
    }

    @Override
    public boolean isPropertiesLoaded() {
        return true;
    }

    @Override
    public void load() {
        // no op as do not want to load from fs
    }

    @Override
    public void store() {
        // no op as does not want to store on fs
    }
}