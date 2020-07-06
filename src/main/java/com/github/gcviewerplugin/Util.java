package com.github.gcviewerplugin;

import com.tagtraum.perf.gcviewer.model.GCResource;

public class Util {

    public static String getNormalizedName(GCResource gcResource) {
        String name = gcResource.getResourceName().replaceAll("\\\\", "/");

        if (name.contains("/")) {
            return name.substring(name.lastIndexOf("/") + 1);
        }

        return name;
    }
}
