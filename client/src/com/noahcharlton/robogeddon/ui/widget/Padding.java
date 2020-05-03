package com.noahcharlton.robogeddon.ui.widget;import com.noahcharlton.robogeddon.ui.Scale;public class Padding extends Widget{    private Widget widget;    private int left;    private int right;    private int top;    private int bottom;    public Padding(Widget widget) {        this.widget = widget;        add(widget);    }    @Override    public void layout() {        widget.setX(getX() + left);        widget.setY(getY() + bottom);        var width = (widget.getWidth() / Scale.scale) + left + right;        var height = (widget.getHeight() / Scale.scale) + bottom + top;        widget.setWidth(getWidth() - left - right);        widget.setHeight(getHeight() - bottom - top);        setWidthScaled(width);        setHeightScaled(height);    }    public Padding setWidget(Widget widget) {        this.widget = widget;        invalidateParent();        getChildren().clear();        add(widget);        return this;    }    public Padding all(int i) {        top(i);        left(i);        right(i);        bottom(i);        return this;    }    public Padding top(int top) {        this.top = top;        invalidateParent();        return this;    }    public Padding bottom(int bottom) {        this.bottom = bottom;        invalidateParent();        return this;    }    public Padding left(int left) {        this.left = left;        invalidateParent();        return this;    }    public Padding right(int right) {        this.right = right;        invalidateParent();        return this;    }    @Override    public boolean isVisible() {        return super.isVisible() && widget.isVisible();    }    @Override    public String toString() {        return "Padding(" + widget + ")";    }}