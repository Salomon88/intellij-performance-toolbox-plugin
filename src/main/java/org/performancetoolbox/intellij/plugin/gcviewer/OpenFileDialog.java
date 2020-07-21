package org.performancetoolbox.intellij.plugin.gcviewer;

import com.intellij.openapi.project.Project;
import com.tagtraum.perf.gcviewer.model.GCResource;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.DialogWrapperWIthResultAndHistory;

import static org.performancetoolbox.intellij.plugin.common.Util.createGCResource;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class OpenFileDialog extends DialogWrapperWIthResultAndHistory<GCResource> {

    private GCResource result;

    public OpenFileDialog(@Nullable Project project) {
        super(project,
                getResourceBundle().getString("dialog.open.gc.title"),
                getResourceBundle().getString("dialog.open.gc.text"),
                "performancetoolbox.open.gc.urls");
    }

    @Override
    public GCResource getResult() {
        return result;
    }

    @Override
    protected void prepareResult() {
        result = createGCResource(textFieldWithHistory.getText().split(";"));
    }
}