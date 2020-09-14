package org.performancetoolbox.intellij.plugin.gcviewer;

import com.intellij.openapi.project.Project;
import com.tagtraum.perf.gcviewer.model.GCResource;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.DialogWrapperWIthResultAndHistory;
import org.performancetoolbox.intellij.plugin.common.OpenFileHistoryAdapter;

import static org.performancetoolbox.intellij.plugin.common.Util.createGCResource;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.common.Util.getUnpackedHistoryRecord;
import static org.performancetoolbox.intellij.plugin.common.bundles.Bundle.getString;

public class OpenFileDialog extends DialogWrapperWIthResultAndHistory<GCResource> {

    private GCResource result;

    public OpenFileDialog(@Nullable Project project, OpenFileHistoryAdapter historyAdapter) {
        super(project,
                getString("dialog.open.gc.title"),
                getString("dialog.open.gc.text"),
                historyAdapter);
    }

    @Override
    public GCResource getResult() {
        return result;
    }

    @Override
    protected void prepareResult() {
        result = createGCResource(getUnpackedHistoryRecord(textFieldWithHistory.getText())).orElseThrow();
    }
}