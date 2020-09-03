package org.performancetoolbox.intellij.plugin.gcviewer;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import org.apache.commons.collections.map.LRUMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.util.Map;

import static org.apache.commons.collections.MapUtils.isNotEmpty;

@State(
        name = PreferencesComponent.COMPONENT_NAME,
        storages = @Storage("gcviewerPreferences.xml")
)
public class PreferencesComponent implements PersistentStateComponent<PreferencesComponent> {

    public static final String COMPONENT_NAME = "gcviewerPreferences";
    private static final int GCDOC_MAP_CAPACITY = 15;

    @MapAnnotation(entryTagName = "filepath", surroundWithTag = false)
    private final Map<String, PreferenceData> map = new LRUMap(GCDOC_MAP_CAPACITY);

    @Nullable
    @Override
    public PreferencesComponent getState() {
        return this;
    }

    @Override
    public void loadState(PreferencesComponent state) {
        map.putAll(state.map);
    }

    public void setGcDocPreference(@NotNull String filePath, @NotNull PropertyChangeListener propertyChangeListener) {
        filePath = formatFilepath(filePath);
        if (!map.containsKey(filePath)) {
            map.put(filePath, new PreferenceData());
        }
        map.get(filePath).addPropertyChangeListener(propertyChangeListener);
    }

    public PreferenceData getPreferenceData(@NotNull String filePath) {
        filePath = formatFilepath(filePath);
        if (isNotEmpty(map) && map.containsKey(filePath)) {
            return map.get(filePath);
        } else {
            PreferenceData preferenceData = new PreferenceData();
            map.put(filePath, preferenceData);
            return preferenceData;
        }
    }

    public void removeGcDocListener(@NotNull String filePath) {
        map.get(formatFilepath(filePath)).removePropertyChangeListener();
    }

    private String formatFilepath(String filePath) {
        return filePath.replace("/", "").
                replace("\\", "").
                replace(":", "");
    }
}