package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

public abstract class DialogWrapperWithResult<T> extends DialogWrapper {

    public DialogWrapperWithResult(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
    }

    public abstract T getResult();
}