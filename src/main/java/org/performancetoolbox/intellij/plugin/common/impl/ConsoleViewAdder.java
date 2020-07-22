package org.performancetoolbox.intellij.plugin.common.impl;

import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.performancetoolbox.intellij.plugin.common.ToolContentHoldable;
import org.performancetoolbox.intellij.plugin.common.ViewAdder;

public class ConsoleViewAdder implements ViewAdder {

    @Override
    public void addToView(Project project, ToolContentHoldable toolContentHoldable) {
        ApplicationManager.getApplication().invokeLater(() -> {
            final Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            final ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
            final RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, null, toolContentHoldable.getComponent(), toolContentHoldable.getDisplayName(), toolContentHoldable.getIcon()) {
                {
                    Disposer.register(this, toolContentHoldable);
                }

                @Override
                public boolean isContentReuseProhibited() {
                    return true;
                }
            };
            RunContentManager.getInstance(project).showRunContent(executor, descriptor);
        });
    }
}