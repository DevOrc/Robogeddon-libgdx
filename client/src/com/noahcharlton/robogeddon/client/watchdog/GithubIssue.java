package com.noahcharlton.robogeddon.client.watchdog;

import com.noahcharlton.robogeddon.Core;
import org.lwjgl.Sys;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GithubIssue {

    private static final String baseURL = "https://github.com/DevOrc/Robogeddon/issues/new?";

    public static void open(Throwable error) {
        String url = baseURL;

        url += "title=";
        url += URLEncoder.encode(getThrowableMessage(error), StandardCharsets.UTF_8);
        url += "&body=";
        url += URLEncoder.encode(getDetails(error), StandardCharsets.UTF_8);

        Sys.openURL(url);
    }

    private static String getThrowableMessage(Throwable error) {
        return "Game Crash: " + (error.getMessage() == null ? error.getClass().getSimpleName() : error.getMessage());
    }

    private static String getDetails(Throwable error) {
        StringWriter writer = new StringWriter();
        writer.write("```java\n");
        error.printStackTrace(new PrintWriter(writer));
        writer.write("```\n\n ##### This is an auto generated bug report \n\n");
        writer.write("Game Version: " + Core.VERSION + "-" + Core.VERSION_TYPE);

        String output = writer.toString();

        if(output.length() > 20000){
            return output.substring(0, 20000).concat(" ...");
        }

        return output;
    }
}
