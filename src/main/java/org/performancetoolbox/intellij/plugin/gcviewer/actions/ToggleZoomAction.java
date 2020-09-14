package org.performancetoolbox.intellij.plugin.gcviewer.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.performancetoolbox.intellij.plugin.gcviewer.ToolContentHolder;

import static com.intellij.icons.AllIcons.General.ZoomIn;
import static java.lang.Double.parseDouble;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public class ToggleZoomAction extends ToggleAction {

    private final ToolContentHolder toolContentHolder;

    public ToggleZoomAction(ToolContentHolder toolContentHolder, ZoomPercent zoomPercent) {
        super(zoomPercent.getValue());
        this.toolContentHolder = toolContentHolder;
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return toolContentHolder.getModelChart().getScaleFactor() == getScale(e);
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        toolContentHolder.getModelChart().setScaleFactor(getScale(e));
    }

    private double getScale(@NotNull AnActionEvent e) {
        String rawScale = e.getPresentation().getText();
        return parseDouble(rawScale.substring(0, rawScale.length() - 1));
    }

    public static class ZoomActionGroup extends DefaultActionGroup {
        public ZoomActionGroup(ToolContentHolder toolContentHolderImpl) {
            super(getString("action.gc.zoom.text"), true);
            getTemplatePresentation().setIcon(ZoomIn);
            for (ZoomPercent percent : ZoomPercent.values()) {
                add(new ToggleZoomAction(toolContentHolderImpl, percent));
            }
        }
    }

    private enum ZoomPercent {
        Z1("1%"),
        Z5("5%"),
        Z10("10%"),
        Z50("50%"),
        Z100("100%"),
        Z200("200%"),
        Z300("300%"),
        Z500("500%"),
        Z1000("1000%"),
        Z5000("5000%");

        final String value;

        ZoomPercent(String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }
    }
}