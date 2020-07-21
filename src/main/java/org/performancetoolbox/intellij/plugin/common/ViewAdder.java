package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.project.Project;

public interface ViewAdder {
    void addToView(Project project, ToolContentHoldable toolContentHoldable);
}