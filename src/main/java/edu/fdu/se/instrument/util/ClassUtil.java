package edu.fdu.se.instrument.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.jar.JarFile;

public class ClassUtil {


    public static byte[] getClassByte(String jarPath, String className) throws MalformedURLException, ClassNotFoundException {
        String classPrefix = className.substring(0, className.lastIndexOf('.') - 1);
        classPrefix = classPrefix.replaceAll("\\.", "/");
        String classResourceName = classPrefix + className.substring(className.lastIndexOf('.'));

        try (
                JarFile jarFile = new JarFile(jarPath);
                InputStream in = jarFile.getInputStream(jarFile.getJarEntry(classResourceName))
        ) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            IOUtil.copy(in, bao);
            return bao.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static Boolean isArray(String className) {
        if (className == null) return false;
        return className.startsWith("[") || className.contains("[]");
    }

    public static byte[] getClassBytes(ClassLoader loader, String className) throws IOException {
        String internalName = className.replace('.', '/') + ".class";
        InputStream inputStream = loader.getResourceAsStream(internalName);
        if (inputStream != null) {
            return IOUtil.readBytes(inputStream);
        } else {
            return null;
        }
    }
}
