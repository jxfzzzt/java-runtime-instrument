package edu.fdu.se.instrument;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import edu.fdu.se.instrument.record.MethodInvocationRecord;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExecutorTest {

    private static List<String> jarPaths;

    @BeforeClass
    public static void setUp() {
        String projectDir = System.getProperty("user.dir");
        File projectFile = FileUtil.file(projectDir, "src/test/resources/test-instrument-project");
        File jarFile = FileUtil.file(projectFile, "target/test-instrument-project-1.0.jar");
        Assert.isTrue(jarFile.exists());

        jarPaths = new ArrayList<>();
        jarPaths.add(jarFile.getAbsolutePath());
    }

    @Test
    public void testInstrumentAndExecute() {
        MethodInvocationRecord record = Executor.instrumentAndExecute(jarPaths, "edu.fdu.se.test.service.impl.UserServiceImpl", "getUserById", new Class[]{int.class}, new Object[]{5});
        System.out.println(record.getMethodExecuteSigSet());
    }
}
