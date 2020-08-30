package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.nio.file.Files.writeString;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ToolContentDataLoaderTest {

    @TempDir
    public Path tempDir;

    @Test
    void sanity() throws IOException {
        String module = "testModule";
        String name = "testName";
        long instances = 128;
        long size = 1024;

        File file = Paths.get(tempDir.toString(), "histogram").toFile();
        writeString(file.toPath(), format("1: %d %d %s (%s)", instances, size, name, module));
        ToolContentDataLoader dataLoader = new ToolContentDataLoader(singletonList(new LightVirtualFile(file.getPath())));
        dataLoader.execute();

        assertNotNull(dataLoader.getContentData());
        assertEquals(1, dataLoader.getContentData().getClassStates().size());
        assertEquals(instances, (long) dataLoader.getContentData().getClassStates().get(0).getFinalInstances());
        assertEquals(module, dataLoader.getContentData().getClassStates().get(0).getModule());
        assertEquals(name, dataLoader.getContentData().getClassStates().get(0).getName());
        assertEquals(size, (long) dataLoader.getContentData().getClassStates().get(0).getFinalSize());
    }

    @Test
    void differenceCalculator1() throws Exception {
        List<VirtualFile> files = range(1, 7)
                .mapToObj(index -> new LightVirtualFile(Paths.get(tempDir.toString(), Integer.toString(index)).toString()))
                .collect(toList());
        writeString(new File(files.get(0).getPath()).toPath(), "1: 10 20 testName");
        writeString(new File(files.get(1).getPath()).toPath(), "");
        writeString(new File(files.get(2).getPath()).toPath(), "1: 5 10 testName");
        writeString(new File(files.get(3).getPath()).toPath(), "");
        writeString(new File(files.get(4).getPath()).toPath(), "1: 20 40 testName");
        writeString(new File(files.get(5).getPath()).toPath(), "1: 40 80 testName");

        ToolContentDataLoader dataLoader = new ToolContentDataLoader(files);
        dataLoader.execute();

        assertEquals(1, dataLoader.getContentData().getClassStates().size());

        assertEquals(10L, (long) dataLoader.getContentData().getClassStates().get(0).getInitialInstances());
        assertEquals(40L, (long) dataLoader.getContentData().getClassStates().get(0).getFinalInstances());
        assertArrayEquals(new Long[]{null, -5L, null, 15L, 20L}, dataLoader.getContentData().getClassStates().get(0).getDifferencesInstances());

        assertEquals(20L, (long) dataLoader.getContentData().getClassStates().get(0).getInitialSize());
        assertEquals(80L, (long) dataLoader.getContentData().getClassStates().get(0).getFinalSize());
        assertArrayEquals(new Long[]{null, -10L, null, 30L, 40L}, dataLoader.getContentData().getClassStates().get(0).getDifferencesSizes());
    }

    @Test
    void differenceCalculator2() throws Exception {
        List<VirtualFile> files = range(1, 4)
                .mapToObj(index -> new LightVirtualFile(Paths.get(tempDir.toString(), Integer.toString(index)).toString()))
                .collect(toList());
        writeString(new File(files.get(0).getPath()).toPath(), "");
        writeString(new File(files.get(1).getPath()).toPath(), "1: 10 20 testName");
        writeString(new File(files.get(2).getPath()).toPath(), "1: 20 40 testName");

        ToolContentDataLoader dataLoader = new ToolContentDataLoader(files);
        dataLoader.execute();

        assertEquals(1, dataLoader.getContentData().getClassStates().size());

        assertNull(dataLoader.getContentData().getClassStates().get(0).getInitialInstances());
        assertEquals(20L, (long) dataLoader.getContentData().getClassStates().get(0).getFinalInstances());
        assertArrayEquals(new Long[]{10L, 10L}, dataLoader.getContentData().getClassStates().get(0).getDifferencesInstances());

        assertNull(dataLoader.getContentData().getClassStates().get(0).getInitialSize());
        assertEquals(40L, (long) dataLoader.getContentData().getClassStates().get(0).getFinalSize());
        assertArrayEquals(new Long[]{20L, 20L}, dataLoader.getContentData().getClassStates().get(0).getDifferencesSizes());
    }
}