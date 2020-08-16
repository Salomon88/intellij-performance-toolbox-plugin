package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;

public class State {

    private List<ClassState> classStates;
    private List<VirtualFile> files;

    public State(List<VirtualFile> files, List<ClassState> classStates) {
        this.classStates = classStates;
        this.files = files;
    }

    public List<ClassState> getClassStates() {
        return new ArrayList<>(classStates);
    }

    public List<VirtualFile> getFiles() {
        return new ArrayList<>(files);
    }

    public static class ClassState {
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

        public ClassState setDifferencesInstances(Long[] differencesInstances) {
            this.differencesInstances = differencesInstances;
            return this;
        }

        public ClassState setDifferencesSizes(Long[] differencesSizes) {
            this.differencesSizes = differencesSizes;
            return this;
        }

        public ClassState setInitialInstances(Long initialInstances) {
            this.initialInstances = initialInstances;
            return this;
        }

        public ClassState setInitialSize(Long initialSize) {
            this.initialSize = initialSize;
            return this;
        }

        public ClassState setFinalInstances(Long finalInstances) {
            this.finalInstances = finalInstances;
            return this;
        }

        public ClassState setFinalSize(Long finalSize) {
            this.finalSize = finalSize;
            return this;
        }

        public ClassState setModule(String module) {
            this.module = module;
            return this;
        }

        public ClassState setName(String name) {
            this.name = name;
            return this;
        }
    }
}