package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class Scene extends Widget {

    @Override
    public void layout() {
        setX(0);
        setY(0);

        for(Widget widget : getChildren()) {
            layoutChild(widget);
        }
    }

    @Override
    public boolean isMouseOver(){
        for(Widget widget : getChildren()) {
            if(widget.isMouseOver())
                return true;
        }

        return false;
    }

    private void layoutChild(Widget widget) {
        widget.invalidate();

        switch(widget.getAlign()) {
            case Align.center:
                var x = (getWidth() / 2) - (widget.getWidth() / 2);
                var y = (getHeight() / 2) - (widget.getHeight() / 2);
                widget.setPosition(x, y);
                break;
            case Align.left:
                widget.setX(0);
                widget.setY((getHeight() / 2) - (widget.getHeight() / 2));
                break;
            case Align.right:
                widget.setX(getWidth() - widget.getWidth());
                widget.setY((getHeight() / 2) - (widget.getHeight() / 2));
                break;
            case Align.top:
                widget.setX((getWidth() / 2) - (widget.getWidth() / 2));
                widget.setY(getHeight() - widget.getHeight());
                break;
            case Align.bottom:
                widget.setX((getWidth() / 2) - (widget.getWidth() / 2));
                widget.setY(0);
                break;
            case Align.bottomLeft:
                widget.setPosition(0, 0);
                break;
            case Align.topLeft:
                widget.setPosition(0, getHeight() - widget.getHeight());
                break;
            case Align.bottomRight:
                widget.setPosition(getWidth() - widget.getWidth(), 0);
                break;
            case Align.topRight:
                widget.setPosition(getWidth() - widget.getWidth(), getHeight() - widget.getHeight());
                break;
            default:
                Log.warn("Unknown align: " + Align.toString(widget.getAlign()));
        }
    }

    public void onResize(int width, int height) {
        //Do not scale this because it is the screen size
        setSize(width / Scale.scale, height / Scale.scale);
        this.invalidate();
    }
}
