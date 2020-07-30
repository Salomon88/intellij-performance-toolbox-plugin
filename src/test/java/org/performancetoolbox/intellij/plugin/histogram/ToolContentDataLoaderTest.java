package org.performancetoolbox.intellij.plugin.histogram;

import com.intellij.testFramework.LightVirtualFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.lang.String.format;
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
}