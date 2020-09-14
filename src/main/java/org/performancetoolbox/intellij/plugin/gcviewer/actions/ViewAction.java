package org.performancetoolbox.intellij.plugin.gcviewer.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.tagtraum.perf.gcviewer.view.renderer.ConcurrentGcBegionEndRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.FullGCLineRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.GCRectanglesRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.IncLineRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.InitialMarkLevelRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.TotalHeapRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.TotalTenuredRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.UsedHeapRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.UsedTenuredRenderer;
import com.tagtraum.perf.gcviewer.view.renderer.UsedYoungRenderer;
import com.tagtraum.perf.gcviewer.view.util.ImageHelper;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.bundles.Bundle;
import org.performancetoolbox.intellij.plugin.gcviewer.PreferenceData;

import javax.swing.*;

import static com.intellij.icons.AllIcons.General.Filter;
import static com.tagtraum.perf.gcviewer.util.LocalisationHelper.getString;

public abstract class ViewAction extends ToggleAction {

    private boolean state;

    public ViewAction(@Nullable @Nls(capitalization = Nls.Capitalization.Title) String text,
                      @Nullable Icon icon,
                      boolean state
    ) {

        this(text, icon, null, state);
    }

    public ViewAction(@Nullable @Nls(capitalization = Nls.Capitalization.Title) String text,
                      @Nullable Icon icon,
                      @Nullable String description,
                      boolean state
    ) {
        super(text, description, icon);
        this.state = state;
        getTemplatePresentation().setIcon(icon);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return state;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        this.state = state;
        notifyGui(state);
    }

    abstract void notifyGui(boolean is);

    public static class ViewActionGroup extends DefaultActionGroup {

        public ViewActionGroup(PreferenceData preferenceData) {
            super(Bundle.getString("action.gc.document.view.filter"), true);
            getTemplatePresentation().setIcon(Filter);

            add(new ViewAction(getString("main_frame_menuitem_full_gc_lines"),
                    ImageHelper.createMonoColoredImageIcon(FullGCLineRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isFullGcLines()
            ) {

                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setFullGcLines(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_inc_gc_lines"),
                    ImageHelper.createMonoColoredImageIcon(IncLineRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isIncGcLines()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setIncGcLines(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_gc_times_line"),
                    ImageHelper.createMonoColoredImageIcon(IncLineRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isGcTimesLine()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setGcTimesLine(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_gc_times_rectangles"),
                    ImageHelper.createMonoColoredImageIcon(GCRectanglesRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isGcTimesRectangles()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setGcTimesRectangles(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_total_memory"),
                    ImageHelper.createMonoColoredImageIcon(TotalHeapRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isTotalMemory()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setTotalMemory(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_tenured_memory"),
                    ImageHelper.createMonoColoredImageIcon(TotalTenuredRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isTenuredMemory()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setTenuredMemory(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_used_memory"),
                    ImageHelper.createMonoColoredImageIcon(UsedHeapRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isUsedMemory()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setUsedMemory(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_used_tenured_memory"),
                    ImageHelper.createMonoColoredImageIcon(UsedTenuredRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isUsedTenuredMemory()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setUsedTenuredMemory(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_used_young_memory"),
                    ImageHelper.createMonoColoredImageIcon(UsedYoungRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isUsedYoungMemory()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setUsedYoungMemory(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_initial_mark_level"),
                    ImageHelper.createMonoColoredImageIcon(InitialMarkLevelRenderer.DEFAULT_LINEPAINT, 20, 20),
                    preferenceData.isInitialMarkLevel()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setInitialMarkLevel(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_concurrent_collection_begin_end"),
                    ImageHelper.createMonoColoredImageIcon(ConcurrentGcBegionEndRenderer.CONCURRENT_COLLECTION_BEGIN, 20, 20),
                    preferenceData.isConcurrentCollectionBeginEnd()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setConcurrentCollectionBeginEnd(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_antialias"),
                    ImageHelper.createEmptyImageIcon(20, 20),
                    preferenceData.isAntiAlias()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setAntiAlias(is);
                }
            });

            add(new ViewAction(getString("main_frame_menuitem_show_date_stamp"),
                    ImageHelper.createEmptyImageIcon(20, 20),
                    preferenceData.isShowDatestamp()
            ) {
                @Override
                public void notifyGui(boolean is) {
                    preferenceData.setShowDatestamp(is);
                }
            });
        }
    }
}