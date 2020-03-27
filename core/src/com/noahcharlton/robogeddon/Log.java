package com.noahcharlton.robogeddon;

public class Log {

    public static void info(String text){
        log("INFO", text);
    }

    public static void warn(String text){
        log("WARN", text);
    }

    public static void error(String text){
        log("ERROR", text);
    }

    public static void debug(String text){
        log("DEBUG", text);
    }

    public static void trace(String text){
        log("TRACE", text);
    }

    public static void log(String type, String info){
        System.out.printf("%s(%s): %s\n", Thread.currentThread().getName(), type, info);
    }
}
