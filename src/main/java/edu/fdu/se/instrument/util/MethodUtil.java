package edu.fdu.se.instrument.util;

import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodUtil {

    public static String getMethodSignature(String className, String methodName, org.objectweb.asm.Type returnType, org.objectweb.asm.Type[] argTypes) {
        String sb = "<" +
                className +
                ": " +
                corp(returnType.getClassName()) +
                " " +
                methodName +
                "(" +
                Arrays.stream(argTypes)
                        .map(org.objectweb.asm.Type::getClassName)
                        .map(MethodUtil::corp)
                        .collect(Collectors.joining(",")) +
                ")" +
                ">";
        return sb;
    }

    // obtain soot method signature
    public static String getMethodSignature(Class clazz, Method method) {
        String sb = "<" +
                clazz.getName() +
                ": " +
                corp(method.getReturnType().getName()) +
                " " +
                method.getName() +
                "(" +
                Arrays.stream(method.getParameterTypes())
                        .map(Class::getName)
                        .map(MethodUtil::corp)
                        .collect(Collectors.joining(",")) +
                ")" +
                ">";
        return sb;
    }

    public static String getMethodSignature(Class clazz, Constructor constructor) {
        String sb = "<" +
                clazz.getName() +
                ": " +
                "void <init>" +
                "(" +
                Arrays.stream(constructor.getParameterTypes())
                        .map(Class::getName)
                        .map(MethodUtil::corp)
                        .collect(Collectors.joining(",")) +
                ")" +
                ">";
        return sb;
    }


    private static String getNameOfType(String s) {
        int index = s.lastIndexOf(".");
        String shortName = s.substring(index + 1);
        return shortName;
    }

    public static String getMethodName(Method method) {
        return method.getName();
    }

    public static String getMethodDescriptor(Method method) {
        return org.objectweb.asm.Type.getMethodDescriptor(method);
    }

    public static String getMethodName(Constructor constructor) {
        return "<init>";
    }

    public static String getMethodDescriptor(Constructor constructor) {
        return org.objectweb.asm.Type.getConstructorDescriptor(constructor);
    }

    public static boolean containSetFieldMethod(Class clazz, Field field) {
        String fieldName = field.getName();
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = null;
        try {
            method = clazz.getMethod(setterName, field.getType());
        } catch (NoSuchMethodException e) {
            // do nothing
        }
        return method != null;
    }

    private static String corp(String name) {
        if (name.charAt(0) == '[') {
            int j = 0;
            int cnt = 0;
            while (name.charAt(j) == '[') {
                j++;
                cnt++;
            }

            if (name.charAt(j) == 'L') j++;

            name = name.substring(j);

            if (name.charAt(name.length() - 1) == ';') {
                name = name.substring(0, name.length() - 1);
            }

            switch (name) {
                case "V":
                    name = "void";
                    break;
                case "Z":
                    name = "boolean";
                    break;
                case "B":
                    name = "byte";
                    break;
                case "C":
                    name = "char";
                    break;
                case "S":
                    name = "short";
                    break;
                case "I":
                    name = "int";
                    break;
                case "J":
                    name = "long";
                    break;
                case "F":
                    name = "float";
                    break;
                case "D":
                    name = "double";
                    break;
                default:
                    break;
            }

            StringBuilder sb = new StringBuilder(name);
            for (int i = 0; i < cnt; i++) sb.append("[]");
            name = sb.toString();
            return name;
        } else {
            return name;
        }
    }
}
