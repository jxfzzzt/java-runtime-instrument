package edu.fdu.se.instrument.util;

import com.nqzero.permit.Permit;
import java.lang.reflect.*;

public class ReflectionUtil {

    public static void setAccessible(AccessibleObject member) {
        String versionStr = System.getProperty("java.version");
        int javaVersion = Integer.parseInt(versionStr.split("\\.")[0]);
        if (javaVersion < 12) {
            Permit.setAccessible(member);
        } else {
            member.setAccessible(true);
        }
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            setAccessible(field);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }

    public static Object getFieldValue(final Object obj, final String fieldName) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        return field.get(obj);
    }

    public static Constructor<?> getFirstCtor(final String name) throws Exception {
        final Constructor<?> ctor = Class.forName(name).getDeclaredConstructors()[0];
        setAccessible(ctor);
        return ctor;
    }

    public static Object newInstance(String className, Object... args) throws Exception {
        return getFirstCtor(className).newInstance(args);
    }

    public static Method getMethodByName(Class<?> clazz, String methodName) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public static boolean isGetMethod(String methodName) {
        if (methodName.length() > 3) {
            return methodName.startsWith("get");
        } else {
            return false;
        }
    }

    public static String attributeFromGetMethod(String methodName) {
        if (isGetMethod(methodName)) {
            String attributeName = methodName.substring(3);
            return attributeName.substring(0, 1).toLowerCase() + attributeName.substring(1);
        } else {
            return null;
        }
    }
}
