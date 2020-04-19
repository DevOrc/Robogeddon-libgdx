package com.noahcharlton.robogeddon.ui.widget;

public class Stack extends Widget {

    private int spacing = 5;

    @Override
    public void layout() {
        var childrenCount = getChildren().size();

        float y = getY();
        float startY = y;
        float width = getMinWidth();

        for(int i = childrenCount - 1; i >= 0; i--) {
            var widget = getChildren().get(i);

            if(!widget.isVisible())
                continue;

            widget.setX(getX());
            widget.setY(y);

            y += widget.getHeight() + spacing;
            width = Math.max(width, widget.getWidth());
        }

        setHeight(y - startY);
        setWidth(width);
        getChildren().forEach(child -> child.setWidth(getWidth()));
    }
    public Stack setSpacing(int spacing) {
        this.spacing = spacing;
        this.invalidate();
        return this;
    }
}
