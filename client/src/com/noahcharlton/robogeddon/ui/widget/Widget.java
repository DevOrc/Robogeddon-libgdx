package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.background.Background;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Widget {

    protected static ShapeDrawer shapeDrawer;

    private final List<Widget> children = new ArrayList<>();

    private float x;
    private float y;
    private float width;
    private float height;
    private int align = Align.center;
    private Background background;

    private Widget parent;
    private boolean invalidated;

    public final void render(SpriteBatch batch) {
        if(invalidated)
            validate();

        if(background != null)
            background.draw(batch, this);

        draw(batch);
        children.forEach(widget -> widget.render(batch));
        update();
    }

    public void draw(SpriteBatch batch){}

    private void validate() {
        invalidated = false;
        layout();
    }

    public void layout(){}

    public void update(){

    }

    public void add(Widget... widgets){
        var newChildren = Arrays.asList(widgets);

        newChildren.forEach(w -> w.parent = this);
        this.children.addAll(newChildren);
    }

    public void invalidate(){
        this.invalidated = true;
    }

    public void invalidateParent(){
        if(parent != null)
            parent.invalidate();
    }

    public void invalidateHierarchy(){
        invalidated = true;

        if(parent != null)
            parent.invalidateHierarchy();
    }

    public List<Widget> getChildren() {
        return children;
    }

    public Widget setPosition(float x, float y){
        this.x = x;
        this.y = y;

        return this;
    }

    public Widget setSize(float width, float height){
        this.width = width;
        this.height = height;

        return this;
    }

    public Widget setX(float x) {
        this.x = x;
        return this;
    }

    public Widget setY(float y) {
        this.y = y;
        return this;
    }

    public Widget setWidth(float width) {
        this.width = width;
        return this;
    }

    public Widget setHeight(float height) {
        this.height = height;
        return this;
    }

    public Widget setBackground(Background background) {
        this.background = background;
        return this;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Background getBackground() {
        return background;
    }

    public Widget align(int align) {
        this.align = align;
        return this;
    }

    public int getAlign() {
        return align;
    }

    public boolean isInvalidated() {
        return invalidated;
    }

    public Widget getParent() {
        return parent;
    }

    public static void setShapeDrawer(ShapeDrawer shapeDrawer) {
        if(Widget.shapeDrawer != null)
            throw new RuntimeException("Cannot set shape drawer twice.");
        Widget.shapeDrawer = shapeDrawer;
    }

    public static ShapeDrawer getShapeDrawer() {
        return shapeDrawer;
    }
}
