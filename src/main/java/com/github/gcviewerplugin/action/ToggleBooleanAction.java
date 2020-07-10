package com.github.gcviewerplugin.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ToggleBooleanAction extends ToggleAction {

    private boolean state;

    public ToggleBooleanAction(String text, String description, Icon icon, boolean state) {
        super(() -> text, () -> description, icon);
        this.state = state;
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return state;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        this.state = state;
    }
}