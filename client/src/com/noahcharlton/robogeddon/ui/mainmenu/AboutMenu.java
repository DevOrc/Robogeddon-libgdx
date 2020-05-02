package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.GLVersion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Link;
import com.noahcharlton.robogeddon.ui.widget.Stack;

public class AboutMenu extends MainMenuSubScreen {

    public AboutMenu() {
        super("About");

        var stack = new Stack().setSpacing(10);

        var centerLabelColor = new Color(.9f, .9f, .9f, 1f);
        for(String info : getAboutInfo()){
            if(info.contains("!link=")){
                var infoArray = info.split("!link=", 2);
                var text = infoArray[0];
                var link = infoArray[1];

                stack.add(new Link().setLink(link).setText(text));
            }else{
                stack.add(new Label(info).setTextColor(centerLabelColor));
            }
        }

        add(stack);
    }

    private String[] getAboutInfo() {
        GLVersion glInfo = Gdx.graphics.getGLVersion();

        return new String[]{
                "Author: Noah Charlton",
                "Game Version: " + Core.VERSION,
                "Version Type: " + Core.VERSION_TYPE,
                "LibGDX Version: " + Version.VERSION,
                "GL Version: " + glInfo.getMajorVersion() + "." + glInfo.getMinorVersion(),
                "Vendor: " + glInfo.getVendorString(),
                "Java Version: " + System.getProperty("java.version"),
                "OS: " + System.getProperty("os.name"),
                "",
                "Links:",
                "Github (Source Code) !link=https://github.com/DevOrc/Robogeddon",
                "Report a bug !link=https://github.com/DevOrc/Robogeddon/issues/new",
                "Trello Board !link=https://trello.com/b/wLF3CEK6/robogeddon",
        };
    }
}
