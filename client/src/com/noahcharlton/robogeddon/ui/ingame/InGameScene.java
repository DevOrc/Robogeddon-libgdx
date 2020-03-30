package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class InGameScene extends Scene {

    private final Widget widget = new TextButton("Hello, please click me!");
    private int alignIndex = 0;

    public InGameScene() {
        add(widget);
        add(new InventoryList()).align(Align.topLeft);
    }

    @Override
    public void update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            alignIndex++;
            invalidate();
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            alignIndex--;
            invalidate();
        }

        if(alignIndex < 0){
            alignIndex = 0;
            invalidate();
        }else if(alignIndex > 8){
            alignIndex = 8;
            invalidate();
        }

        updateAlign();

    }

    private void updateAlign() {
        switch(alignIndex){
            case 0:
                widget.align(Align.bottomLeft);
                break;
            case 1:
                widget.align(Align.bottom);
                break;
            case 2:
                widget.align(Align.bottomRight);
                break;
            case 3:
                widget.align(Align.left);
                break;
            case 4:
                widget.align(Align.center);
                break;
            case 5:
                widget.align(Align.right);
                break;
            case 6:
                widget.align(Align.topLeft);
                break;
            case 7:
                widget.align(Align.top);
                break;
            case 8:
                widget.align(Align.topRight);
                break;
        }
    }
}
