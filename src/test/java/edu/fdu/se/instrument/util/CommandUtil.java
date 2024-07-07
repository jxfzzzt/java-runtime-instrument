package edu.fdu.se.instrument.util;

import cn.hutool.log.StaticLog;

import java.io.*;

public class CommandUtil {

    public static int execCommand(String command) throws IOException, InterruptedException {
        String[] commandArr = command.split(" ");
        Process process = Runtime.getRuntime().exec(commandArr);

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        // output the process detail
        while ((line = reader.readLine()) != null) {
            StaticLog.info("exec command line output: {}", line);
        }
        return process.waitFor();
    }

    public static int execCommand(File file, String command) throws IOException, InterruptedException {
        return execCommand(file, command, false);
    }

    public static int execCommand(File file, String command, boolean logging) throws IOException, InterruptedException {
        String[] commandArr = command.split(" ");

        Process process = Runtime.getRuntime().exec(commandArr, null, file);

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        // output the process detail
        while ((line = reader.readLine()) != null) {
            if (logging) {
                StaticLog.info("exec command line output: {}", line);
            }
        }
        return process.waitFor();
    }

}
