package com.noahcharlton.robogeddon.ui.widget;

import com.noahcharlton.robogeddon.ui.background.Background;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;

import java.util.function.BiConsumer;

public abstract class Button extends Widget {

    private BiConsumer<ClickEvent, Button> onClick;
    private Background defaultBackground = new ColorBackground();
    private Background onHover = new ColorBackground();

    public Button() {
    }

    @Override
    public void update() {
        if(isMouseOver()){
            setBackground(onHover);
        }else{
            setBackground(defaultBackground);
        }
    }

    @Override
    protected void onClick(ClickEvent event) {
        if(onClick != null)
            onClick.accept(event, this);
    }

    public Button setDefaultBackground(Background defaultBackground) {
        this.defaultBackground = defaultBackground;
        return this;
    }

    public Button setOnHover(Background onHover) {
        this.onHover = onHover;
        return this;
    }

    public Button setOnClick(BiConsumer<ClickEvent, Button> onClick) {
        this.onClick = onClick;
        return this;
    }
}
