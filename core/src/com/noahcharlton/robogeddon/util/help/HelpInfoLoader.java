package com.noahcharlton.robogeddon.util.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlReader;

@Side(Side.CLIENT)
public class HelpInfoLoader {

    private static final ObjectMap<String, HelpInfo> helpInfo = new ObjectMap<>();
    private static final HelpInfo defaultEntry
            = new BasicHelpInfo("No entry found!", "If you see this page, please report this as a bug!");

    public static void init(){
        Log.info("Loading help info!");

        FileHandle file = Gdx.files.internal("help_info.xml");
        Element reader = new XmlReader().parse(file.readString());

        for(Element entry : reader.getChildrenByName("BasicHelpPage")){
            var info = createHelpInfo(entry);

            helpInfo.put(entry.getAttribute("id"), info);
        }
    }

    public static HelpInfo getEntry(String id){
        return helpInfo.get(id, null);
    }

    private static HelpInfo createHelpInfo(Element element) {
        String title = element.get("Title");
        String desc = element.get("Desc");
        desc = desc.replaceAll("\\s+", " ").replace("[NL]", "\n");

        System.out.println(title + ": \n" + desc);
        return new BasicHelpInfo(title, desc);
    }

    public static class BasicHelpInfo implements HelpInfo{

        public static final String guiID = "basic_help_info";

        private final String title;
        private final String desc;

        public BasicHelpInfo(String title, String desc) {
            this.title = title;
            this.desc = desc;
        }

        public String getHelpPageText() {
            return desc;
        }

        public String getHelpPageTitle() {
            return title;
        }

        @Override
        public String getGuiID() {
            return guiID;
        }
    }

}
