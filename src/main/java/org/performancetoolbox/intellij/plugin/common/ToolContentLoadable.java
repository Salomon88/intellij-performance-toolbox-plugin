package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.project.Project;

import java.util.function.BiConsumer;

public interface ToolContentLoadable<T> {
    void load(T input, BiConsumer<Project, ToolContentHoldable> callback);
}