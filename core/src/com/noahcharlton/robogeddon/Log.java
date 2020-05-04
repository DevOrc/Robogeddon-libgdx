package com.noahcharlton.robogeddon;

public class Log {

    public static boolean debug = false;
    public static boolean trace = false;

    public static void info(String text){
        log("INFO", text);
    }

    public static void info(String text, Throwable e){
        log("INFO", text, e);
    }

    public static void warn(String text){
        log("WARN", text);
    }

    public static void warn(String text, Throwable e){
        log("WARN", text, e);
    }

    public static void error(String text){
        log("ERROR", text);
    }

    public static void error(String text, Throwable e){
        log("ERROR", text, e);
    }

    public static void debug(String text){
        if(debug){
            log("DEBUG", text);
        }
    }

    public static void debug(String text, Throwable e){
        if(debug){
            log("DEBUG", text, e);
        }
    }

    public static void trace(String text){
        if(trace && debug){
            log("TRACE", text);
        }
    }

    public static void trace(String text, Throwable e){
        if(trace && debug){
            log("TRACE", text, e);
        }
    }

    public static void log(String type, String info, Throwable e){
        System.out.printf("%s(%s): %s: %s\n", Thread.currentThread().getName(), type, info, e.getLocalizedMessage());
    }

    public static void log(String type, String info){
        System.out.printf("%s(%s): %s\n", Thread.currentThread().getName(), type, info);
    }
}
