package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.TextFieldWithHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.List;

import static com.intellij.openapi.fileChooser.FileChooser.chooseFiles;
import static com.intellij.ui.GuiUtils.constructFieldWithBrowseButton;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static org.performancetoolbox.intellij.plugin.common.Util.getHistoryRecord;

public abstract class DialogWrapperWIthResultAndHistory<T> extends DialogWrapperWithResult<T> {

    protected OpenFileHistoryAdapter historyAdapter;
    protected Project myProject;
    protected String text;
    protected TextFieldWithHistory textFieldWithHistory;

    public DialogWrapperWIthResultAndHistory(@Nullable Project project, String title, String text, OpenFileHistoryAdapter historyAdapter) {
        super(project, true);
        this.historyAdapter = historyAdapter;
        this.myProject = project;
        this.text = text;
        init();
        loadHistory();
        setResizable(false);
        setTitle(title);
        updateOKActionEnabled();
    }

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
        textFieldWithHistory.addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                updateOKActionEnabled();
            }
        });
        textFieldWithHistory.setMinimumAndPreferredWidth(600);
        return constructFieldWithBrowseButton(textFieldWithHistory, actionEvent -> {
            FileChooserDescriptor fcd = new FileChooserDescriptor(true, false, false, false, false, true);
            chooseFiles(fcd, myProject, null, files -> textFieldWithHistory.setText(getHistoryRecord(files)));
        });
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

    protected void updateOKActionEnabled() {
        setOKActionEnabled(textFieldWithHistory.getText() != null && !textFieldWithHistory.getText().isEmpty());
    }
}