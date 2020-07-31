package org.performancetoolbox.intellij.plugin.gcviewer;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.*;
import static org.apache.commons.collections.MapUtils.isNotEmpty;

@State(name = PreferencesComponent.COMPONENT_NAME, storages = @Storage("gcviewerPreferences.xml"))
public class PreferencesComponent implements PersistentStateComponent<PreferencesComponent> {

    public static final String COMPONENT_NAME = "gcviewerPreferences";

    @MapAnnotation(entryTagName = "filepath", surroundWithTag = false)
    private Map<String, PreferenceData> map = new HashMap<>();

    @Nullable
    @Override
    public PreferencesComponent getState() {
        return this;
    }

    @Override
    public void loadState(PreferencesComponent state) {
        map.putAll(state.map);
    }

    public void setGcDocPreference(@NotNull String filePath) {
        filePath = formatFilepath(filePath);
        if (!map.containsKey(filePath)) map.put(filePath, new PreferenceData());
    }

    public PreferenceData getPreferenceData(@NotNull String filePath) {
        filePath = formatFilepath(filePath);
        return isNotEmpty(map) && map.containsKey(filePath) ? map.get(filePath) : new PreferenceData();
    }

    public void addGcDocListener(@NotNull String filePath, @NotNull PropertyChangeListener propertyChangeListener) {
        filePath = formatFilepath(filePath);
        map.get(filePath).addPropertyChangeListener(propertyChangeListener);
    }

    public void removeGcDocListener(@NotNull String filePath, @NotNull PropertyChangeListener propertyChangeListener) {
        map.get(filePath).removePropertyChangeListener(propertyChangeListener);
    }

    private String formatFilepath(String filePath) {
        return filePath.replace("/", "");
    }

    @Tag("preferencedata")
    public static final class PreferenceData {

        @Tag("antiAlias")
        private boolean antiAlias = true;
        @Tag("concurrentCollectionBeginEnd")
        private boolean concurrentCollectionBeginEnd = true;
        @Tag("fullGcLines")
        private boolean fullGcLines = true;
        @Tag("incGcLines")
        private boolean incGcLines = true;
        @Tag("initialMarkLevel")
        private boolean initialMarkLevel = true;
        @Tag("gcTimesLine")
        private boolean gcTimesLine = true;
        @Tag("gcTimesRectangles")
        private boolean gcTimesRectangles = true;
        @Tag("showDatestamp")
        private boolean showDatestamp = true;
        @Tag("tenuredMemory")
        private boolean tenuredMemory = true;
        @Tag("totalMemory")
        private boolean totalMemory = true;
        @Tag("usedMemory")
        private boolean usedMemory = true;
        @Tag("usedTenuredMemory")
        private boolean usedTenuredMemory = true;
        @Tag("usedYoungMemory")
        private boolean usedYoungMemory = true;

        private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

        public boolean isAntiAlias() {
            return antiAlias;
        }

        public void setAntiAlias(boolean antiAlias) {
            if (this.antiAlias != antiAlias) {
                propertyChangeSupport.firePropertyChange(ANTI_ALIAS, this.antiAlias, antiAlias);
            }

            this.antiAlias = antiAlias;
        }

        public boolean isConcurrentCollectionBeginEnd() {
            return concurrentCollectionBeginEnd;
        }

        public void setConcurrentCollectionBeginEnd(boolean concurrentCollectionBeginEnd) {
            if (this.concurrentCollectionBeginEnd != concurrentCollectionBeginEnd) {
                propertyChangeSupport.firePropertyChange(CONCURRENT_COLLECTION_BEGIN_END, this.concurrentCollectionBeginEnd, concurrentCollectionBeginEnd);
            }

            this.concurrentCollectionBeginEnd = concurrentCollectionBeginEnd;
        }

        public boolean isFullGcLines() {
            return fullGcLines;
        }

        public void setFullGcLines(boolean fullGcLines) {
            if (this.fullGcLines != fullGcLines) {
                propertyChangeSupport.firePropertyChange(FULL_GC_LINES, this.fullGcLines, fullGcLines);
            }

            this.fullGcLines = fullGcLines;
        }

        public boolean isIncGcLines() {
            return incGcLines;
        }

        public void setIncGcLines(boolean incGcLines) {
            if (this.incGcLines != incGcLines) {
                propertyChangeSupport.firePropertyChange(INC_GC_LINES, this.incGcLines, incGcLines);
            }

            this.incGcLines = incGcLines;
        }

        public boolean isInitialMarkLevel() {
            return initialMarkLevel;
        }

        public void setInitialMarkLevel(boolean initialMarkLevel) {
            if (this.initialMarkLevel != initialMarkLevel) {
                propertyChangeSupport.firePropertyChange(INITIAL_MARK_LEVEL, this.initialMarkLevel, initialMarkLevel);
            }

            this.initialMarkLevel = initialMarkLevel;
        }

        public boolean isGcTimesLine() {
            return gcTimesLine;
        }

        public void setGcTimesLine(boolean gcTimesLine) {
            if (this.gcTimesLine != gcTimesLine) {
                propertyChangeSupport.firePropertyChange(GC_TIMES_LINE, this.gcTimesLine, gcTimesLine);
            }

            this.gcTimesLine = gcTimesLine;
        }

        public boolean isGcTimesRectangles() {
            return gcTimesRectangles;
        }

        public void setGcTimesRectangles(boolean gcTimesRectangles) {
            if (this.gcTimesRectangles != gcTimesRectangles) {
                propertyChangeSupport.firePropertyChange(GC_TIMES_RECTANGLES, this.gcTimesRectangles, gcTimesRectangles);
            }

            this.gcTimesRectangles = gcTimesRectangles;
        }

        public boolean isShowDatestamp() {
            return showDatestamp;
        }

        public void setShowDatestamp(boolean showDatestamp) {
            if (this.showDatestamp != showDatestamp) {
                propertyChangeSupport.firePropertyChange(SHOW_DATE_STAMP, this.showDatestamp, showDatestamp);
            }

            this.showDatestamp = showDatestamp;
        }

        public boolean isTenuredMemory() {
            return tenuredMemory;
        }

        public void setTenuredMemory(boolean tenuredMemory) {
            if (this.tenuredMemory != tenuredMemory) {
                propertyChangeSupport.firePropertyChange(TENURED_MEMORY, this.tenuredMemory, tenuredMemory);
            }

            this.tenuredMemory = tenuredMemory;
        }

        public boolean isTotalMemory() {
            return totalMemory;
        }

        public void setTotalMemory(boolean totalMemory) {
            if (this.totalMemory != totalMemory) {
                propertyChangeSupport.firePropertyChange(TOTAL_MEMORY, this.totalMemory, totalMemory);
            }

            this.totalMemory = totalMemory;
        }

        public boolean isUsedMemory() {
            return usedMemory;
        }

        public void setUsedMemory(boolean usedMemory) {
            if (this.usedMemory != usedMemory) {
                propertyChangeSupport.firePropertyChange(USED_MEMORY, this.usedMemory, usedMemory);
            }

            this.usedMemory = usedMemory;
        }

        public boolean isUsedTenuredMemory() {
            return usedTenuredMemory;
        }

        public void setUsedTenuredMemory(boolean usedTenuredMemory) {
            if (this.usedTenuredMemory != usedTenuredMemory) {
                propertyChangeSupport.firePropertyChange(USED_TENURED_MEMORY, this.usedTenuredMemory, usedTenuredMemory);
            }

            this.usedTenuredMemory = usedTenuredMemory;
        }

        public boolean isUsedYoungMemory() {
            return usedYoungMemory;
        }

        public void setUsedYoungMemory(boolean usedYoungMemory) {
            if (this.usedYoungMemory != usedYoungMemory) {
                propertyChangeSupport.firePropertyChange(USED_YOUNG_MEMORY, this.usedYoungMemory, usedYoungMemory);
            }

            this.usedYoungMemory = usedYoungMemory;
        }

        void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }
}
