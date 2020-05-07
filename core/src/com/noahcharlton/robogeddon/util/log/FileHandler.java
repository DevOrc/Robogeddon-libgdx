package com.noahcharlton.robogeddon.util.log;

import com.badlogic.gdx.files.FileHandle;

import java.io.PrintWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class FileHandler extends Handler {

    private final FileHandle handle;
    private final PrintWriter writer;

    public FileHandler(String path) {
        handle = new FileHandle(path);
        writer = new PrintWriter(handle.write(false));
    }

    @Override
    public void publish(LogRecord record) {
        if(isLoggable(record)){
            var text = getFormatter().format(record);

            synchronized(writer){
                writer.print(text);
                writer.flush();
            }
        }
    }

    @Override
    public void flush() {
        writer.flush();
    }

    @Override
    public void close() throws SecurityException {
        writer.close();
    }
}
