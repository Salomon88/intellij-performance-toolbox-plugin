package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.Nullable;
import org.performancetoolbox.intellij.plugin.common.DialogWrapperWIthResultAndHistory;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class OpenFileDialog extends DialogWrapperWIthResultAndHistory<List<VirtualFile>> {

    private List<VirtualFile> result;

    public OpenFileDialog(@Nullable Project project) {
        super(project,
                getResourceBundle().getString("dialog.open.histogram.title"),
                getResourceBundle().getString("dialog.open.histogram.text"),
                "performancetoolbox.open.histograms.urls");
    }

    @Override
    public List<VirtualFile> getResult() {
        return result;
    }

    @Override
    protected void prepareResult() {
        result = Arrays.stream(textFieldWithHistory.getText().split(";")).map(LightVirtualFile::new).collect(toList());
    }
}
