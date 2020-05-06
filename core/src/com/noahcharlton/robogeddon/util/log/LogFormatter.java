package com.noahcharlton.robogeddon.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    private static final int MAX_STACKTRACE_LENGTH = 50000;

    @Override
    public String format(LogRecord record) {
        if(record.getThrown() == null){
            return String.format("%s(%s): %s\n",
                    Thread.currentThread().getName(),
                    getLevelText(record.getLevel()),
                    record.getMessage());
        }else{
            String thrownMessage = getThrownMessage(record);
            String stackTrace = getStacktrace(record.getThrown());

            return String.format("%s(%s): %s: %s \n%s\n",
                    Thread.currentThread().getName(),
                    getLevelText(record.getLevel()),
                    record.getMessage(),
                    thrownMessage,
                    stackTrace);
        }
    }

    private String getThrownMessage(LogRecord record) {
        var thrownMessage = record.getThrown().getLocalizedMessage();

        if(thrownMessage == null){
            thrownMessage = record.getThrown().getClass().getName();
        }
        return thrownMessage;
    }

    private String getLevelText(Level level) {
        if(level == Level.SEVERE){
            return "ERROR";
        } else if(level == Level.WARNING){
            return "WARN";
        }else if(level == Level.INFO){
            return "INFO";
        }else if(level == Level.FINE){
            return "DEBUG";
        }else if(level == Level.FINER){
            return "TRACE";
        }

        return level.getLocalizedName();
    }

    private String getStacktrace(Throwable error) {
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));

        String output = writer.toString();

        if(output.length() > MAX_STACKTRACE_LENGTH){
            return output.substring(0, MAX_STACKTRACE_LENGTH).concat(" ...");
        }

        return output;
    }
}
