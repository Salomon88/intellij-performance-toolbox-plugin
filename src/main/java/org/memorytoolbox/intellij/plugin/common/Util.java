package org.memorytoolbox.intellij.plugin.common;

import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.view.GCDocument;

import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class Util {

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("gcviewerBundle");
    }

    public static String getNormalizedName(GCDocument gcDocument) {
        return getNormalizedName(gcDocument.getGCResources().get(0));
    }

    public static String getNormalizedName(GCResource gcResource) {
        String name = gcResource.getResourceName().replaceAll("\\\\", "/");

        if (name.contains("/")) {
            return name.substring(name.lastIndexOf("/") + 1);
        }

        return name;
    }

    public static <T extends PropertyChangeListener> T getPropertyChangeListener(GCDocument document, Class<T> clazz) {
        for (PropertyChangeListener listener : document.getPropertyChangeListeners()) {
            if (clazz.isInstance(listener)) {
                return clazz.cast(listener);
            }
        }

        throw new IllegalStateException("Failed to find listener of class " + clazz);
    }
}