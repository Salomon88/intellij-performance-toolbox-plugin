package org.performancetoolbox.intellij.plugin.common.bundles;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public class Bundle extends DynamicBundle {

    @NonNls
    public static final String BUNDLE_PATH = "gcviewerBundle";
    public static final Bundle INSTANCE = new Bundle();

    private Bundle() {
        super(BUNDLE_PATH);
    }

    @NotNull
    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE_PATH) String key, Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

    @NotNull
    public static Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE_PATH) String key, Object @NotNull ... params) {
        return INSTANCE.getLazyMessage(key, params);
    }

    public static String getString(@PropertyKey(resourceBundle = BUNDLE_PATH) final String key) {
        return message(key);
    }
}