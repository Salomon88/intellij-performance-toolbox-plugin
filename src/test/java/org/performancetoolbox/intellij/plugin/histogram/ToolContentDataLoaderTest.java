package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.nio.file.Files.writeString;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ToolContentDataLoaderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void sanity() throws IOException {
        String module = "testModule";
        String name = "testName";
        long instances = 128;
        long size = 1024;

        File file = temporaryFolder.newFile("histogram");
        writeString(file.toPath(), format("1: %d %d %s (%s)", instances, size, name, module));
        ToolContentDataLoader dataLoader = new ToolContentDataLoader(singletonList(new LightVirtualFile(file.getPath())));
        dataLoader.execute();

        assertNotNull(dataLoader.getContentData());
        assertEquals(1, dataLoader.getContentData().size());
        assertEquals(instances, (long) dataLoader.getContentData().get(0).getFinalInstances());
        assertEquals(module, dataLoader.getContentData().get(0).getModule());
        assertEquals(name, dataLoader.getContentData().get(0).getName());
        assertEquals(size, (long) dataLoader.getContentData().get(0).getFinalSize());
    }

    @Test
    public void differenceCalculator1() throws Exception {
        List<VirtualFile> files = asList(
                new LightVirtualFile(temporaryFolder.newFile("1").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("2").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("3").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("4").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("5").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("6").getPath()));
        writeString(Paths.get(files.get(0).getPath()), "1: 10 20 testName");
        writeString(Paths.get(files.get(1).getPath()), "");
        writeString(Paths.get(files.get(2).getPath()), "1: 5 10 testName");
        writeString(Paths.get(files.get(3).getPath()), "");
        writeString(Paths.get(files.get(4).getPath()), "1: 20 40 testName");
        writeString(Paths.get(files.get(5).getPath()), "1: 40 80 testName");

        ToolContentDataLoader dataLoader = new ToolContentDataLoader(files);
        dataLoader.execute();

        assertEquals(1, dataLoader.getContentData().size());

        assertEquals(10L, (long) dataLoader.getContentData().get(0).getInitialInstances());
        assertEquals(40L, (long) dataLoader.getContentData().get(0).getFinalInstances());
        assertArrayEquals(new Long[]{null, -5L, null, 15L, 20L}, dataLoader.getContentData().get(0).getDifferencesInstances());

        assertEquals(20L, (long) dataLoader.getContentData().get(0).getInitialSize());
        assertEquals(80L, (long) dataLoader.getContentData().get(0).getFinalSize());
        assertArrayEquals(new Long[]{null, -10L, null, 30L, 40L}, dataLoader.getContentData().get(0).getDifferencesSizes());
    }

    @Test
    public void differenceCalculator2() throws Exception {
        List<VirtualFile> files = asList(
                new LightVirtualFile(temporaryFolder.newFile("1").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("2").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("3").getPath()));
        writeString(Paths.get(files.get(0).getPath()), "");
        writeString(Paths.get(files.get(1).getPath()), "1: 10 20 testName");
        writeString(Paths.get(files.get(2).getPath()), "1: 20 40 testName");

        ToolContentDataLoader dataLoader = new ToolContentDataLoader(files);
        dataLoader.execute();

        assertEquals(1, dataLoader.getContentData().size());

        assertNull(dataLoader.getContentData().get(0).getInitialInstances());
        assertEquals(20L, (long) dataLoader.getContentData().get(0).getFinalInstances());
        assertArrayEquals(new Long[]{10L, 10L}, dataLoader.getContentData().get(0).getDifferencesInstances());

        assertNull(dataLoader.getContentData().get(0).getInitialSize());
        assertEquals(40L, (long) dataLoader.getContentData().get(0).getFinalSize());
        assertArrayEquals(new Long[]{20L, 20L}, dataLoader.getContentData().get(0).getDifferencesSizes());
    }
}