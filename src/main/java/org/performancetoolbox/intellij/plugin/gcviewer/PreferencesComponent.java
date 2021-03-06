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
import static org.performancetoolbox.intellij.plugin.common.Util.DEFAULT_PREFERENCES_CAPACITY;
import static org.performancetoolbox.intellij.plugin.common.Util.formatFilepath;

@State(name = PreferencesComponent.COMPONENT_NAME, storages = @Storage("gcviewerPreferences.xml"))
public class PreferencesComponent implements PersistentStateComponent<PreferencesComponent> {

    public static final String COMPONENT_NAME = "gcviewerPreferences";

    @MapAnnotation(entryTagName = "filepath", surroundWithTag = false)
    private final Map<String, PreferenceData> map = new LRUMap(DEFAULT_PREFERENCES_CAPACITY);

    @Nullable
    @Override
    public PreferencesComponent getState() {
        return this;
    }

    @Override
    public void loadState(PreferencesComponent state) {
        map.putAll(state.map);
    }

    /*
    Initialize main method
     */
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
}