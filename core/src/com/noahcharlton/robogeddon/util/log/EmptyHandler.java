package com.noahcharlton.robogeddon.util.log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class EmptyHandler extends Handler {

    @Override
    public void publish(LogRecord record) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
