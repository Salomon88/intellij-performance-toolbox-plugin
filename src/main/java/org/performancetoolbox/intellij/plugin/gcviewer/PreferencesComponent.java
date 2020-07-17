package org.performancetoolbox.intellij.plugin.gcviewer;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.ANTI_ALIAS;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.CONCURRENT_COLLECTION_BEGIN_END;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.FULL_GC_LINES;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.GC_TIMES_LINE;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.GC_TIMES_RECTANGLES;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.INC_GC_LINES;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.INITIAL_MARK_LEVEL;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.SHOW_DATE_STAMP;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.TENURED_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.TOTAL_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.USED_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.USED_TENURED_MEMORY;
import static com.tagtraum.perf.gcviewer.view.model.GCPreferences.USED_YOUNG_MEMORY;

@State(
        name = PreferencesComponent.COMPONENT_NAME,
        storages = {
                @Storage("gcviewerPreferences.xml")
        }
)
public class PreferencesComponent implements PersistentStateComponent<PreferencesComponent> {

    public static final String COMPONENT_NAME = "gcviewerPreferences";

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private boolean antiAlias = true;
    private boolean concurrentCollectionBeginEnd = true;
    private boolean fullGcLines = true;
    private boolean incGcLines = true;
    private boolean initialMarkLevel = true;
    private boolean gcTimesLine = true;
    private boolean gcTimesRectangles = true;
    private boolean showDatestamp = true;
    private boolean tenuredMemory = true;
    private boolean totalMemory = true;
    private boolean usedMemory = true;
    private boolean usedTenuredMemory = true;
    private boolean usedYoungMemory = true;

    @Nullable
    @Override
    public PreferencesComponent getState() {
        PreferencesComponent preferencesComponent = new PreferencesComponent();
        preferencesComponent.loadState(this);
        return preferencesComponent;
    }

    @Override
    public void loadState(@NotNull PreferencesComponent state) {
        setAntiAlias(state.isAntiAlias());
        setConcurrentCollectionBeginEnd(state.isConcurrentCollectionBeginEnd());
        setFullGcLines(state.isFullGcLines());
        setIncGcLines(state.isIncGcLines());
        setInitialMarkLevel(state.isInitialMarkLevel());
        setGcTimesLine(state.isGcTimesLine());
        setGcTimesRectangles(state.isGcTimesRectangles());
        setShowDatestamp(state.isShowDatestamp());
        setTenuredMemory(state.isTenuredMemory());
        setTotalMemory(state.isTotalMemory());
        setUsedMemory(state.isUsedMemory());
        setUsedTenuredMemory(state.isUsedTenuredMemory());
        setUsedYoungMemory(state.isUsedYoungMemory());
    }

    void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

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
}
