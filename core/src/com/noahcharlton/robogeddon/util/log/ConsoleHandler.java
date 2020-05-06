package com.noahcharlton.robogeddon.util.log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ConsoleHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        if(isLoggable(record))
            System.out.print(getFormatter().format(record));
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
