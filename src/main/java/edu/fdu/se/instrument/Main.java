package edu.fdu.se.instrument;

import edu.fdu.se.instrument.record.MethodInvocationRecord;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> jarPaths = new ArrayList<>();
        jarPaths.add("/Users/zhouzhuotong/java_projects/java-runtime-instrument/src/test/resources/test-instrument-project/target/test-instrument-project-1.0.jar");
        MethodInvocationRecord record = Executor.instrumentAndExecute(jarPaths, "edu.fdu.se.test.service.impl.UserServiceImpl", "getUserById", new Class[]{int.class}, new Object[]{5});
        System.out.println(record.getMethodExecuteSigSet());
    }
}
