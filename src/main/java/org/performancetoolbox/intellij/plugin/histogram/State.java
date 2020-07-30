package org.performancetoolbox.intellij.plugin.histogram;

public class State {
    private Long initialInstances;
    private Long initialSize;
    private Long finalInstances;
    private Long finalSize;
    private Long[] differencesInstances;
    private Long[] differencesSizes;
    private String module;
    private String name;

    public Long getInitialInstances() {
        return initialInstances;
    }

    public Long getInitialSize() {
        return initialSize;
    }

    public Long getFinalInstances() {
        return finalInstances;
    }

    public Long getFinalSize() {
        return finalSize;
    }

    public Long[] getDifferencesInstances() {
        return differencesInstances;
    }

    public Long[] getDifferencesSizes() {
        return differencesSizes;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public void setDifferencesInstances(Long[] differencesInstances) {
        this.differencesInstances = differencesInstances;
    }

    public void setDifferencesSizes(Long[] differencesSizes) {
        this.differencesSizes = differencesSizes;
    }

    public void setInitialInstances(Long initialInstances) {
        this.initialInstances = initialInstances;
    }

    public void setInitialSize(Long initialSize) {
        this.initialSize = initialSize;
    }

    public void setFinalInstances(Long finalInstances) {
        this.finalInstances = finalInstances;
    }

    public void setFinalSize(Long finalSize) {
        this.finalSize = finalSize;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setName(String name) {
        this.name = name;
    }
}