package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(
        name = org.performancetoolbox.intellij.plugin.gcviewer.PreferencesComponent.COMPONENT_NAME,
        storages = @Storage("histogramPreferences.xml")
)
public class PreferencesComponent implements PersistentStateComponent<PreferencesComponent> {

    @Override
    public @Nullable PreferencesComponent getState() {
        return null;
    }

    @Override
    public void loadState(@NotNull PreferencesComponent state) {

    }
}
