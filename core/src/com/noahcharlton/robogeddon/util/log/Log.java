package com.noahcharlton.robogeddon.util.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    public static final Level consoleLevel = Level.INFO;
    public static final Level fileLevel = Level.FINE;

    private static Logger logger;

    public static void init(){
        var consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LogFormatter());
        consoleHandler.setLevel(consoleLevel);


        var fileHandler = createFileHandler();
        fileHandler.setFormatter(new LogFormatter());
        fileHandler.setLevel(fileLevel);

        logger = Logger.getLogger("com.noahcharlton.robogeddon");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        logger.addHandler(fileHandler);

        info("Logger initialized!");
    }

    private static Handler createFileHandler() {
        var logFolder = System.getProperty("user.dir") + "/logs/";
        var fileName = "" + getDate() + ".log";
        var path = logFolder + fileName;

        return new FileHandler(path);
    }

    private static String getDate() {
        DateFormat formatter = new SimpleDateFormat("MM-dd hhmm a ssSSS");
        return formatter.format(new Date());
    }

    public static void info(String text) {
        log(Level.INFO, text);
    }

    public static void info(String text, Throwable e) {
        log(Level.INFO, text, e);
    }

    public static void warn(String text) {
        log(Level.WARNING, text);
    }

    public static void warn(String text, Throwable e) {
        log(Level.WARNING, text, e);
    }

    public static void error(String text) {
        log(Level.SEVERE, text);
    }

    public static void error(String text, Throwable e) {
        log(Level.SEVERE, text, e);
    }

    public static void debug(String text) {
        log(Level.FINE, text);
    }

    public static void debug(String text, Throwable e) {
        log(Level.FINE, text, e);
    }

    public static void trace(String text) {
        log(Level.FINER, text);
    }

    public static void trace(String text, Throwable e) {
        log(Level.FINER, text, e);
    }

    private static void log(Level level, String info, Throwable e) {
        if(logger == null){
            init();
            warn("Logger was never initialized!");
        }

        logger.log(level, info, e);
    }

    private static void log(Level level, String info) {
        if(logger == null){
            init();
            warn("Logger was never initialized!");
        }

        logger.log(level, info);
    }
}
