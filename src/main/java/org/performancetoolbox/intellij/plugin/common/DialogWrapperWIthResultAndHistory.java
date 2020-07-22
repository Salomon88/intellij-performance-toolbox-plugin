package org.performancetoolbox.intellij.plugin.common;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.TextFieldWithHistory;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.openapi.fileChooser.FileChooser.chooseFiles;
import static com.intellij.ui.GuiUtils.constructFieldWithBrowseButton;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

public abstract class DialogWrapperWIthResultAndHistory<T> extends DialogWrapperWithResult<T> {

    protected Project myProject;
    protected String propertiesComponentKey;
    protected String text;
    protected TextFieldWithHistory textFieldWithHistory;

    public DialogWrapperWIthResultAndHistory(@Nullable Project project, String title, String text, String propertiesComponentKey) {
        super(project, true);
        this.myProject = project;
        this.propertiesComponentKey = propertiesComponentKey;
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
            chooseFiles(fcd, myProject, null, files -> {
                textFieldWithHistory.setText(files.stream()
                        .map(VirtualFile::getPath)
                        .map(FileUtil::toSystemDependentName)
                        .sorted()
                        .collect(Collectors.joining(";")));
            });
        });
    }

    @Override
    protected void doOKAction() {
        storeHistory();
        prepareResult();
        close(OK_EXIT_CODE);
    }

    protected void loadHistory() {
        final List<String> history = new ArrayList<>();
        final String savedUrl = PropertiesComponent.getInstance().getValue(propertiesComponentKey);

        if (savedUrl != null) {
            ContainerUtil.addAll(history, savedUrl.split(":::"));
        }

        textFieldWithHistory.setHistory(history);
        textFieldWithHistory.setHistorySize(10);

        if (!history.isEmpty()) {
            textFieldWithHistory.setSelectedItem(history.get(0));
            textFieldWithHistory.setText(history.get(0));
        }
    }

    protected void storeHistory() {
        final List<String> history = new ArrayList<>(textFieldWithHistory.getHistory());
        history.remove(textFieldWithHistory.getText());

        if (history.isEmpty()) {
            history.add(textFieldWithHistory.getText());
        } else {
            history.add(0, textFieldWithHistory.getText());
        }

        PropertiesComponent.getInstance().setValue(propertiesComponentKey, history.isEmpty() ? null : StringUtil.join(history, ":::"), null);
    }

    protected void updateOKActionEnabled() {
        setOKActionEnabled(textFieldWithHistory.getText() != null && !textFieldWithHistory.getText().isEmpty());
    }
}