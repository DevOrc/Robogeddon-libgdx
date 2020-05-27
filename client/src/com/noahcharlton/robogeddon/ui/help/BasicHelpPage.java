package com.noahcharlton.robogeddon.ui.help;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.util.help.HelpInfo;
import com.noahcharlton.robogeddon.util.help.HelpInfoLoader;

public class BasicHelpPage extends HelpScene {

    public BasicHelpPage(HelpInfo info) {
        super(info);

        var basicInfo = (HelpInfoLoader.BasicHelpInfo) info;
        var title = new Label(basicInfo.getHelpPageTitle()).setFont(UIAssets.largeFont);
        var desc = new Label(basicInfo.getHelpPageText());

        add(title.pad().top(40)).align(Align.top);
        add(desc);
    }

}
