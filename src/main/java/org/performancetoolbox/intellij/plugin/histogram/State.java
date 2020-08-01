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

    public State setDifferencesInstances(Long[] differencesInstances) {
        this.differencesInstances = differencesInstances;
        return this;
    }

    public State setDifferencesSizes(Long[] differencesSizes) {
        this.differencesSizes = differencesSizes;
        return this;
    }

    public State setInitialInstances(Long initialInstances) {
        this.initialInstances = initialInstances;
        return this;
    }

    public State setInitialSize(Long initialSize) {
        this.initialSize = initialSize;
        return this;
    }

    public State setFinalInstances(Long finalInstances) {
        this.finalInstances = finalInstances;
        return this;
    }

    public State setFinalSize(Long finalSize) {
        this.finalSize = finalSize;
        return this;
    }

    public State setModule(String module) {
        this.module = module;
        return this;
    }

    public State setName(String name) {
        this.name = name;
        return this;
    }
}