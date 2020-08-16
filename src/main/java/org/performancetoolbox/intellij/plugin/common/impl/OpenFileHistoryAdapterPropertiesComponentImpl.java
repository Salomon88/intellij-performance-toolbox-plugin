package org.performancetoolbox.intellij.plugin.common.impl;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;
import org.performancetoolbox.intellij.plugin.common.OpenFileHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class OpenFileHistoryAdapterPropertiesComponentImpl implements OpenFileHistoryAdapter {

    private final String propertiesComponentKey;
    private final int historySize;

    public OpenFileHistoryAdapterPropertiesComponentImpl(String propertiesComponentKey, int historySize) {
        this.historySize = historySize;
        this.propertiesComponentKey = propertiesComponentKey;
    }

    @Override
    public List<String> retrieve() {
        final List<String> history = new ArrayList<>();
        final String savedUrl = PropertiesComponent.getInstance().getValue(propertiesComponentKey);

        if (savedUrl != null) {
            ContainerUtil.addAll(history, savedUrl.split(":::"));
        }

        return history;
    }

    @Override
    public int getHistorySize() {
        return historySize;
    }

    @Override
    public void addAndStore(String value) {
        final List<String> history = retrieve();
        history.remove(value);

        if (history.isEmpty()) {
            history.add(value);
        } else {
            history.add(0, value);
        }

        PropertiesComponent.getInstance().setValue(propertiesComponentKey, history.isEmpty() ? null : StringUtil.join(history, ":::"), null);
    }
}