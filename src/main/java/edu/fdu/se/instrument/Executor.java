package edu.fdu.se.instrument;

import edu.fdu.se.instrument.execption.InstrumentException;
import edu.fdu.se.instrument.record.MethodInvocationRecord;
import edu.fdu.se.instrument.runner.MethodInvokeWrapper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Executor {

    private final static long TIME_OUT = 90000L;

    private final static TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    public static MethodInvocationRecord instrumentAndExecute(List<String> jarPaths, String className, String methodName, Class[] parameterTypes, Object... args) throws InstrumentException {
        try {
            ClassLoader classLoader = new InstrumentClassLoader(jarPaths);
            return execute(classLoader, className, methodName, parameterTypes, args);
        } catch (Exception e) {
            throw new InstrumentException("", e);
        }
    }

    private static MethodInvocationRecord execute(ClassLoader classLoader, String className, String methodName, Class[] parameterTypes, Object... args) throws InstrumentException {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        beforeExecute(classLoader);

        Class<?> targetClass;
        Method targetMethod;
        try {
            targetClass = classLoader.loadClass(className);
            targetMethod = targetClass.getMethod(methodName, parameterTypes);
            targetMethod.setAccessible(true);
        } catch (Exception e) {
            throw new InstrumentException("", e);
        }

        new MethodInvokeWrapper(
                () -> targetMethod.invoke(targetClass.newInstance()), TIME_OUT, TIME_UNIT)
                .invoke();

        return afterExecute();
    }

    private static void beforeExecute(ClassLoader classLoader) throws InstrumentException {
        Thread.currentThread().setContextClassLoader(classLoader);

        // reset the stateNode table
        try {
            Class<?> tableClass = classLoader.loadClass(InstrumentClassLoader.STATE_TABLE_CLASS_NAME);
            Method resetMethod = tableClass.getDeclaredMethod("reset");
            resetMethod.invoke(null);
        } catch (Exception e) {
            throw new InstrumentException("instrum", e);
        }
    }

    private static MethodInvocationRecord afterExecute() {
        return null;
    }
}
