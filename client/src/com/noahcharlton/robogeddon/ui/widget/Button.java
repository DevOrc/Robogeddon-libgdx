package com.noahcharlton.robogeddon.ui.widget;

import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;

public class Button extends Widget {

    public Button() {
        setSize(120, 35);
        setBackground(new NinePatchBackground(UIAssets.button));
    }
}
