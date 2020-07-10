package com.github.gcviewerplugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = PreferencesComponent.COMPONENT_NAME,
        storages = {
                @Storage("gcviewerPreferences.xml")
        }
)
public class PreferencesComponent implements PersistentStateComponent<PreferencesComponent> {

    public static final String COMPONENT_NAME = "gcviewerPreferences";

    private boolean antiAlias;
    private boolean concurrentCollectionBeginEnd;
    private boolean fullGcLines;
    private boolean incGcLines;
    private boolean initialMarkLevel;
    private boolean gcTimesLine;
    private boolean gcTimesRectangles;
    private boolean showDatestamp;
    private boolean tenuredMemory;
    private boolean totalMemory;
    private boolean usedMemory;
    private boolean usedTenuredMemory;
    private boolean usedYoungMemory;

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

    public boolean isAntiAlias() {
        return antiAlias;
    }

    public void setAntiAlias(boolean antiAlias) {
        this.antiAlias = antiAlias;
    }

    public boolean isConcurrentCollectionBeginEnd() {
        return concurrentCollectionBeginEnd;
    }

    public void setConcurrentCollectionBeginEnd(boolean concurrentCollectionBeginEnd) {
        this.concurrentCollectionBeginEnd = concurrentCollectionBeginEnd;
    }

    public boolean isFullGcLines() {
        return fullGcLines;
    }

    public void setFullGcLines(boolean fullGcLines) {
        this.fullGcLines = fullGcLines;
    }

    public boolean isIncGcLines() {
        return incGcLines;
    }

    public void setIncGcLines(boolean incGcLines) {
        this.incGcLines = incGcLines;
    }

    public boolean isInitialMarkLevel() {
        return initialMarkLevel;
    }

    public void setInitialMarkLevel(boolean initialMarkLevel) {
        this.initialMarkLevel = initialMarkLevel;
    }

    public boolean isGcTimesLine() {
        return gcTimesLine;
    }

    public void setGcTimesLine(boolean gcTimesLine) {
        this.gcTimesLine = gcTimesLine;
    }

    public boolean isGcTimesRectangles() {
        return gcTimesRectangles;
    }

    public void setGcTimesRectangles(boolean gcTimesRectangles) {
        this.gcTimesRectangles = gcTimesRectangles;
    }

    public boolean isShowDatestamp() {
        return showDatestamp;
    }

    public void setShowDatestamp(boolean showDatestamp) {
        this.showDatestamp = showDatestamp;
    }

    public boolean isTenuredMemory() {
        return tenuredMemory;
    }

    public void setTenuredMemory(boolean tenuredMemory) {
        this.tenuredMemory = tenuredMemory;
    }

    public boolean isTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(boolean totalMemory) {
        this.totalMemory = totalMemory;
    }

    public boolean isUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(boolean usedMemory) {
        this.usedMemory = usedMemory;
    }

    public boolean isUsedTenuredMemory() {
        return usedTenuredMemory;
    }

    public void setUsedTenuredMemory(boolean usedTenuredMemory) {
        this.usedTenuredMemory = usedTenuredMemory;
    }

    public boolean isUsedYoungMemory() {
        return usedYoungMemory;
    }

    public void setUsedYoungMemory(boolean usedYoungMemory) {
        this.usedYoungMemory = usedYoungMemory;
    }
}
