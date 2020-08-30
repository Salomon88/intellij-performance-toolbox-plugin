package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.project.Project;
import org.performancetoolbox.intellij.plugin.common.impl.ConsoleViewAdder;

public class ViewAdderFactory {

    private ViewAdderFactory() {
    }

    public static ViewAdder getInstance() {
        return new ConsoleViewAdder();
    }

    public static void addToView(Project project, ToolContentHoldable toolContentHoldable) {
        getInstance().addToView(project, toolContentHoldable);
    }
}