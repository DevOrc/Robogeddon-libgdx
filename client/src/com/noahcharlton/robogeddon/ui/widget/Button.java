package com.noahcharlton.robogeddon.ui.widget;

import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.Background;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;

public class Button extends Widget {

    private final Background background = new NinePatchBackground(UIAssets.button);
    private final Background onHover = new NinePatchBackground(UIAssets.buttonHover);

    public Button() {
        setBackground(new NinePatchBackground(UIAssets.button));
    }

    @Override
    public void update() {
        if(isMouseOver()){
            setBackground(onHover);
        }else{
            setBackground(background);
        }
    }

    @Override
    protected void onClick(ClickEvent event) {
        System.out.println("On Click");
    }

    @Override
    public void layout() {
        setSize(120, 35);
    }
}
