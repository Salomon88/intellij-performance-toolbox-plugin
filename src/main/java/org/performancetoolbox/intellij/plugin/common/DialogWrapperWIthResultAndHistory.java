package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.TextFieldWithHistory;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.intellij.openapi.fileChooser.FileChooser.chooseFiles;
import static com.intellij.ui.GuiUtils.constructFieldWithBrowseButton;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.util.stream.Collectors.toList;
import static org.performancetoolbox.intellij.plugin.common.Util.PREFERRED_WIDTH;
import static org.performancetoolbox.intellij.plugin.common.Util.getHistoryRecord;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;
import static org.performancetoolbox.intellij.plugin.common.Util.getUnpackedHistoryRecord;

public abstract class DialogWrapperWIthResultAndHistory<T> extends DialogWrapperWithResult<T> {

    protected OpenFileHistoryAdapter historyAdapter;
    protected Project myProject;
    protected String text;
    protected TextFieldWithHistory textFieldWithHistory;

    private String validationInfoText = null;
    private ValidationInfo validationInfo = null;

    public DialogWrapperWIthResultAndHistory(@Nullable Project project, String title, String text, OpenFileHistoryAdapter historyAdapter) {
        super(project, true);
        this.historyAdapter = historyAdapter;
        this.myProject = project;
        this.text = text;
        init();
        loadHistory();
        setResizable(false);
        setTitle(title);
        initValidation();
    }

    /**
     * Must be called on OK action only
     */
    protected abstract void prepareResult();

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        JRootPane pane = getRootPane();
        return pane != null ? pane.getDefaultButton() : super.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(text), NORTH);
        panel.add(createChoosePanel(), CENTER);
        return panel;
    }

    protected JPanel createChoosePanel() {
        textFieldWithHistory = new TextFieldWithHistory();
        textFieldWithHistory.setMinimumAndPreferredWidth(PREFERRED_WIDTH);
        return constructFieldWithBrowseButton(textFieldWithHistory, actionEvent -> {
            FileChooserDescriptor fcd = new FileChooserDescriptor(true, false, false, false, false, true);
            chooseFiles(fcd, myProject, null, files -> textFieldWithHistory.setText(getHistoryRecord(files)));
        });
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        if (Objects.equals(validationInfoText, textFieldWithHistory.getText())) {
            return validationInfo;
        }

        List<VirtualFile> files = getUnpackedHistoryRecord(textFieldWithHistory.getText());
        List<File> invalidFiles = files
                .stream()
                .map(virtualFile -> new File(virtualFile.getPath()))
                .filter(file -> !file.exists() || !file.isFile())
                .collect(toList());

        if (!files.isEmpty() && invalidFiles.isEmpty()) {
            textFieldWithHistory.setText(getHistoryRecord(files));
            validationInfo = null;
        } else if (files.isEmpty()) {
            validationInfo = new ValidationInfo(getResourceBundle().getString("dialog.open.noFileName"), textFieldWithHistory);
        } else {
            validationInfo = new ValidationInfo(getResourceBundle().getString("dialog.open.fileNotFound") + ": " + invalidFiles.get(0).getPath(), textFieldWithHistory);
        }

        validationInfoText = textFieldWithHistory.getText();

        return validationInfo;
    }

    @Override
    protected void doOKAction() {
        storeHistory();
        prepareResult();
        close(OK_EXIT_CODE);
    }

    protected void loadHistory() {
        final List<String> history = historyAdapter.retrieve();
        textFieldWithHistory.setHistory(history);
        textFieldWithHistory.setHistorySize(historyAdapter.getHistorySize());

        if (!history.isEmpty()) {
            textFieldWithHistory.setSelectedItem(history.get(0));
            textFieldWithHistory.setText(history.get(0));
        }
    }

    protected void storeHistory() {
        historyAdapter.addAndStore(textFieldWithHistory.getText());
    }
}