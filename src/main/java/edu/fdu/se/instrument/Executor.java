package edu.fdu.se.instrument;

import cn.hutool.core.lang.Assert;
import edu.fdu.se.instrument.execption.InstrumentException;
import edu.fdu.se.instrument.record.MethodInvocationRecord;
import edu.fdu.se.instrument.runner.MethodInvokeWrapper;
import edu.fdu.se.instrument.state.StateNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Executor {

    // default max execute time
    private final static long TIME_OUT = 90000L;

    private final static TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    public static MethodInvocationRecord instrumentAndExecute(List<String> jarPaths, String className, String methodName, Class[] parameterTypes, Object... args) {
        ClassLoader instrumentClassLoader = null;
        try {
            instrumentClassLoader = new InstrumentClassLoader(jarPaths);
        } catch (MalformedURLException e) {
            throw new InstrumentException("", e);
        }
        return execute(instrumentClassLoader, className, methodName, parameterTypes, args);
    }

    private static MethodInvocationRecord execute(ClassLoader inscurrentClassLoader, String className, String methodName, Class[] parameterTypes, Object... args) {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        beforeExecute(inscurrentClassLoader);

        Class<?> targetClass;
        Method targetMethod = null;
        Constructor<?> targetConstructor = null;

        boolean isExecMethod;

        try {
            targetClass = inscurrentClassLoader.loadClass(className);

            if ("<init>".equals(methodName)) {
                targetConstructor = targetClass.getConstructor(parameterTypes);
                targetConstructor.setAccessible(true);
                isExecMethod = false;
            } else {
                targetMethod = targetClass.getMethod(methodName, parameterTypes);
                targetMethod.setAccessible(true);
                isExecMethod = true;
            }
        } catch (Exception e) {
            throw new InstrumentException("", e);
        }

        try {
            if (isExecMethod) {
                Method finalTargetMethod = targetMethod;
                Assert.notNull(targetClass);
                Assert.notNull(finalTargetMethod);

                if (Modifier.isStatic(finalTargetMethod.getModifiers())) {
                    // resolve execute static method
                    new MethodInvokeWrapper(
                            () -> finalTargetMethod.invoke(null, args), TIME_OUT, TIME_UNIT)
                            .invoke();
                } else {
                    // resolve execute non-static method
                    new MethodInvokeWrapper(
                            () -> finalTargetMethod.invoke(targetClass.newInstance(), args), TIME_OUT, TIME_UNIT)
                            .invoke();
                }
            } else {
                // resolve execute constructor
                Constructor<?> finalTargetConstructor = targetConstructor;
                Assert.notNull(targetClass);
                Assert.notNull(finalTargetConstructor);

                new MethodInvokeWrapper(
                        () -> finalTargetConstructor.newInstance(args), TIME_OUT, TIME_UNIT)
                        .invoke();
            }
        } catch (Exception e) {
            throw new InstrumentException("", e);
        }

        return afterExecute(inscurrentClassLoader, currentClassLoader);
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

    private static MethodInvocationRecord afterExecute(ClassLoader instrumentClassLoader, ClassLoader previousClassLoader) {
        Thread.currentThread().setContextClassLoader(previousClassLoader);

        // get the stateNode and  global table
        try {
            Class<?> tableClass = instrumentClassLoader.loadClass(InstrumentClassLoader.STATE_TABLE_CLASS_NAME);
            Class<?> nodeClass = instrumentClassLoader.loadClass(InstrumentClassLoader.STATE_NODE_CLASS_NAME);

            Method getStateTableMethod = tableClass.getDeclaredMethod("getStateTable");
            Method getSignatureMethod = nodeClass.getDeclaredMethod("getSignature");
            Method getValueMethod = nodeClass.getDeclaredMethod("getValue");
            Method getClazzMethod = nodeClass.getDeclaredMethod("getClazz");

            Map<String, StateNode> copyStateNode = new HashMap<>();

            Map<String, StateNode> stateNodeMap = (Map<String, StateNode>) getStateTableMethod.invoke(null);
            for (String key : stateNodeMap.keySet()) {
                Object stateNode = stateNodeMap.get(key);
                String signature = (String) getSignatureMethod.invoke(stateNode);
                Object value = getValueMethod.invoke(stateNode);
                Class<?> clazz = (Class<?>) getClazzMethod.invoke(stateNode);

                StateNode copyNode = new StateNode(signature, clazz, value);
                copyStateNode.put(key, copyNode);
            }

            Method getMethodSignatureSet = tableClass.getDeclaredMethod("getMethodSignatureSet");
            Set<String> runMethodSigSet = (Set<String>) getMethodSignatureSet.invoke(null);

            Method getInvocationRecordMap = tableClass.getDeclaredMethod("getInvocationRecordMap");
            Map<Integer, String> invocationRecordMap = (Map<Integer, String>) getInvocationRecordMap.invoke(null);

            return new MethodInvocationRecord(instrumentClassLoader, copyStateNode, runMethodSigSet, invocationRecordMap);
        } catch (Exception e) {
            throw new InstrumentException("", e);
        }
    }
}
