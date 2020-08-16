package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.DialogWrapperWIthResultAndHistory;
import org.performancetoolbox.intellij.plugin.common.OpenFileHistoryAdapter;

import java.util.List;

import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.common.Util.getUnpackedHistoryRecord;

public class OpenFileDialog extends DialogWrapperWIthResultAndHistory<List<VirtualFile>> {

    private List<VirtualFile> result;

    public OpenFileDialog(@Nullable Project project, OpenFileHistoryAdapter historyAdapter) {
        super(project,
                getResourceBundle().getString("dialog.open.histogram.title"),
                getResourceBundle().getString("dialog.open.histogram.text"),
                historyAdapter);
    }

    @Override
    public List<VirtualFile> getResult() {
        return result;
    }

    @Override
    protected void prepareResult() {
        result = getUnpackedHistoryRecord(textFieldWithHistory.getText());
    }
}