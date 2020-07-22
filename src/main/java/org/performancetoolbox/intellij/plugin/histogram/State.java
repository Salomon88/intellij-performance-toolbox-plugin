package org.performancetoolbox.intellij.plugin.histogram;

public class State {
    private Long initialSize;
    private Long finalSize;
    private Long[] differences;
    private String module;
    private String name;

    public Long getInitialSize() {
        return initialSize;
    }

    public Long getFinalSize() {
        return finalSize;
    }

    public Long[] getDifferences() {
        return differences;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public void setInitialSize(Long initialSize) {
        this.initialSize = initialSize;
    }

    public void setFinalSize(Long finalSize) {
        this.finalSize = finalSize;
    }

    public void setDifferences(Long[] differences) {
        this.differences = differences;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setName(String name) {
        this.name = name;
    }
}