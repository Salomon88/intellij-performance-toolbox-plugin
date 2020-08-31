package org.performancetoolbox.intellij.plugin.common;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import com.tagtraum.perf.gcviewer.model.GcResourceSeries;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilTest {

    @Test
    void createGCResource() {
        final String[] fileNames1 = new String[]{"/a/b"};
        Optional<GCResource> gcResourceOptional1 = Util.createGCResource(stream(fileNames1).map(LightVirtualFile::new).collect(toList()));
        assertTrue(gcResourceOptional1.isPresent());
        assertTrue(gcResourceOptional1.get() instanceof GcResourceFile);

        final String[] fileNames2 = new String[]{"/a/b", "/a/c"};
        Optional<GCResource> gcResourceOptional2 = Util.createGCResource(stream(fileNames2).map(LightVirtualFile::new).collect(toList()));
        assertTrue(gcResourceOptional2.isPresent());
        assertTrue(gcResourceOptional2.get() instanceof GcResourceSeries);
    }

    @Test
    void getUnpackedHistoryRecord() {
        final String[] fileNames = new String[]{"/a/b", "/a/c"};
        List<VirtualFile> files = Util.getUnpackedHistoryRecord(join(";", fileNames));
        assertNotNull(files);
        assertArrayEquals(fileNames, files.stream().map(VirtualFile::getName).toArray());
    }
}