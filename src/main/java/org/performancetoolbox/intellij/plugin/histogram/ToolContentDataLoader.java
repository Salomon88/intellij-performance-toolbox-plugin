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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;
import static javax.swing.SwingWorker.StateValue.DONE;
import static javax.swing.SwingWorker.StateValue.STARTED;
import static org.performancetoolbox.intellij.plugin.common.Util.getResourceBundle;

public class ToolContentDataLoader implements ToolContentDataLoadable<List<State>> {

    private static final Pattern REGULAR_RECORD = Pattern.compile("\\d+:\\s+(\\d+)\\s+(\\d+)\\s+(\\S+)\\s*(\\((.*)\\))?$");
    private static final Pattern TOTAL_RECORD = Pattern.compile("Total\\s+(\\d+)\\s+(\\d+)$");

    private final List<VirtualFile> files;
    private final Map<String, State> states = new HashMap<>();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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
        for (int index = 0; index < files.size(); index++) {
            try (Scanner scanner = new Scanner(new File(files.get(index).getPath()))) {
                while (scanner.hasNext()) {
                    final String line = scanner.nextLine().trim();
                    parseRegularRecord(line, index);
                    parseTotalRecord(line, index);
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

    private Long getPriorSum(Long initial, Long[] differences, int index) {
        long sum = ofNullable(initial).orElse(0L);

        for (int i = 0; i < index - 1; i++) {
            sum += ofNullable(differences[i]).orElse(0L);
        }

        return sum;
    }

    private String getKey(String className, String moduleName) {
        return className + ":" + moduleName;
    }

    private void parseRecord(String key, String module, String name, int index, long instances, long size) {
        State state;

        if (index == 0) {
            state = new State()
                    .setDifferencesInstances(new Long[files.size() - 1])
                    .setDifferencesSizes(new Long[files.size() - 1])
                    .setInitialInstances(instances)
                    .setInitialSize(size)
                    .setModule(module)
                    .setName(name);
            states.put(key, state);
        } else {
            state = states.get(key);

            if (state == null) {
                state = new State()
                        .setDifferencesInstances(new Long[files.size() - 1])
                        .setDifferencesSizes(new Long[files.size() - 1])
                        .setModule(module)
                        .setName(name);
                state.getDifferencesInstances()[index - 1] = instances;
                state.getDifferencesSizes()[index - 1] = size;
                states.put(key, state);
            } else {
                state.getDifferencesInstances()[index - 1] = instances - getPriorSum(state.getInitialInstances(), state.getDifferencesInstances(), index);
                state.getDifferencesSizes()[index - 1] = size - getPriorSum(state.getInitialSize(), state.getDifferencesSizes(), index);
            }
        }

        if (index == files.size() - 1) {
            state
                    .setFinalInstances(instances)
                    .setFinalSize(size);
        }
    }

    private void parseRegularRecord(String line, int index) {
        Matcher matcher = REGULAR_RECORD.matcher(line);

        if (matcher.matches()) {
            String module = matcher.groupCount() == 5 ? matcher.group(5) : null;
            String name = matcher.group(3);
            String key = getKey(name, module);
            long instances = Long.parseLong(matcher.group(1));
            long size = Long.parseLong(matcher.group(2));

            parseRecord(key, module, name, index, instances, size);
        }
    }

    private void parseTotalRecord(String line, int index) {
        Matcher matcher = TOTAL_RECORD.matcher(line);

        if (matcher.matches()) {
            String name = getResourceBundle().getString("table.histogram.total.name");
            String key = getKey(name, null);
            long instances = Long.parseLong(matcher.group(1));
            long size = Long.parseLong(matcher.group(2));

            parseRecord(key, null, name, index, instances, size);
        }
    }
}