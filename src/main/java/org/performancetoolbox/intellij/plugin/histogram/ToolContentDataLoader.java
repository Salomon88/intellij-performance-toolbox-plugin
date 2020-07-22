package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;
import org.performancetoolbox.intellij.plugin.common.ToolContentDataLoadable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.util.Optional.ofNullable;
import static javax.swing.SwingWorker.StateValue.DONE;
import static javax.swing.SwingWorker.StateValue.STARTED;

public class ToolContentDataLoader implements ToolContentDataLoadable<List<State>> {

    private List<VirtualFile> files;
    private Map<String, State> states = new HashMap<>();
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public ToolContentDataLoader(List<VirtualFile> files) {
        this.files = files;
    }

    @Override
    public List<State> getContentData() {
        return new ArrayList<>(states.values());
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void cancel() {
    }

    @Override
    public void execute() {
        for (int i = 0; i < files.size(); i++) {
            try (Scanner scanner = new Scanner(new File(files.get(i).getPath()))) {
                while (scanner.hasNext()) {
                    String line = scanner.nextLine().trim();

                    if (line.contains(":")) {
                        State state;
                        String[] parts = line.replaceAll("\\s+", " ").split(" ");
                        String module = parts.length > 4 ? parts[4] : null;
                        String name = parts[3];
                        String key = getKey(name, module);

                        if (module != null) {
                            module = module.replace("(", "").replace(")", "");
                        }

                        if (i == 0) {
                            state = new State();
                            state.setDifferences(new Long[files.size() - 1]);
                            state.setInitialSize(Long.parseLong(parts[2]));
                            state.setModule(module);
                            state.setName(name);
                            states.put(key, state);
                        } else {
                            state = states.get(key);

                            if (state == null) {
                                state = new State();
                                state.setDifferences(new Long[files.size() - 1]);
                                state.setModule(module);
                                state.setName(name);
                                state.getDifferences()[i - 1] = Long.parseLong(parts[2]);
                                states.put(key, state);
                            } else {
                                state.getDifferences()[i - 1] = Long.parseLong(parts[2]) - ofNullable(i == 1 ? state.getInitialSize() : state.getDifferences()[i - 2]).orElse(0L);
                            }
                        }

                        if (i == files.size() - 1) {
                            state.setFinalSize(Long.parseLong(parts[2]));
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        propertyChangeSupport.firePropertyChange("state", STARTED, DONE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    private String getKey(String className, String moduleName) {
        return className + ":" + moduleName;
    }
}
