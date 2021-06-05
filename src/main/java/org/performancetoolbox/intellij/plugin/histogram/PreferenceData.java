package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.util.xmlb.annotations.Tag;

import java.util.List;

@Tag("preferenceData")
public class PreferenceData {

    @Tag("order")
    private List<Integer> order;

    public List<Integer> getOrder() {
        return order;
    }

    public void setOrder(List<Integer> order) {
        this.order = order;
    }
}