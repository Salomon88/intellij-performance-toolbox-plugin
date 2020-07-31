package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Files.writeString(file.toPath(), format("1: %d %d %s (%s)", instances, size, name, module));
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
        Files.writeString(Paths.get(files.get(0).getPath()), "1: 10 20 testName");
        Files.writeString(Paths.get(files.get(1).getPath()), "");
        Files.writeString(Paths.get(files.get(2).getPath()), "1: 5 10 testName");
        Files.writeString(Paths.get(files.get(3).getPath()), "");
        Files.writeString(Paths.get(files.get(4).getPath()), "1: 20 40 testName");
        Files.writeString(Paths.get(files.get(5).getPath()), "1: 40 80 testName");

        ToolContentDataLoader dataLoader = new ToolContentDataLoader(files);
        dataLoader.execute();

        assertEquals(1, dataLoader.getContentData().size());

        assertEquals(10L, (long) dataLoader.getContentData().get(0).getInitialInstances());
        assertEquals(40L, (long) dataLoader.getContentData().get(0).getFinalInstances());
        assertEquals(files.size() - 1, dataLoader.getContentData().get(0).getDifferencesInstances().length);
        assertEquals(null, dataLoader.getContentData().get(0).getDifferencesInstances()[0]);
        assertEquals(-5L, (long) dataLoader.getContentData().get(0).getDifferencesInstances()[1]);
        assertEquals(null, dataLoader.getContentData().get(0).getDifferencesInstances()[2]);
        assertEquals(15L, (long) dataLoader.getContentData().get(0).getDifferencesInstances()[3]);
        assertEquals(20L, (long) dataLoader.getContentData().get(0).getDifferencesInstances()[4]);

        assertEquals(20L, (long) dataLoader.getContentData().get(0).getInitialSize());
        assertEquals(80L, (long) dataLoader.getContentData().get(0).getFinalSize());
        assertEquals(files.size() - 1, dataLoader.getContentData().get(0).getDifferencesSizes().length);
        assertEquals(null, dataLoader.getContentData().get(0).getDifferencesSizes()[0]);
        assertEquals(-10L, (long) dataLoader.getContentData().get(0).getDifferencesSizes()[1]);
        assertEquals(null, dataLoader.getContentData().get(0).getDifferencesSizes()[2]);
        assertEquals(30L, (long) dataLoader.getContentData().get(0).getDifferencesSizes()[3]);
        assertEquals(40L, (long) dataLoader.getContentData().get(0).getDifferencesSizes()[4]);
    }

    @Test
    public void differenceCalculator2() throws Exception {
        List<VirtualFile> files = asList(
                new LightVirtualFile(temporaryFolder.newFile("1").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("2").getPath()),
                new LightVirtualFile(temporaryFolder.newFile("3").getPath()));
        Files.writeString(Paths.get(files.get(0).getPath()), "");
        Files.writeString(Paths.get(files.get(1).getPath()), "1: 10 20 testName");
        Files.writeString(Paths.get(files.get(2).getPath()), "1: 20 40 testName");

        ToolContentDataLoader dataLoader = new ToolContentDataLoader(files);
        dataLoader.execute();

        assertEquals(1, dataLoader.getContentData().size());

        assertEquals(null, dataLoader.getContentData().get(0).getInitialInstances());
        assertEquals(20L, (long) dataLoader.getContentData().get(0).getFinalInstances());
        assertEquals(files.size() - 1, dataLoader.getContentData().get(0).getDifferencesInstances().length);
        assertEquals(10L, (long) dataLoader.getContentData().get(0).getDifferencesInstances()[0]);
        assertEquals(10L, (long) dataLoader.getContentData().get(0).getDifferencesInstances()[1]);

        assertEquals(null, dataLoader.getContentData().get(0).getInitialSize());
        assertEquals(40L, (long) dataLoader.getContentData().get(0).getFinalSize());
        assertEquals(files.size() - 1, dataLoader.getContentData().get(0).getDifferencesSizes().length);
        assertEquals(20L, (long) dataLoader.getContentData().get(0).getDifferencesSizes()[0]);
        assertEquals(20L, (long) dataLoader.getContentData().get(0).getDifferencesSizes()[1]);
    }
}