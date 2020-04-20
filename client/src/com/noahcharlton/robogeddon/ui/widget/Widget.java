package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.client.GameClient;
import com.noahcharlton.robogeddon.ui.Scale;
import com.noahcharlton.robogeddon.ui.background.Background;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.event.KeyEvent;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Widget {

    protected static final GameClient client = GameClient.getInstance();
    protected static ShapeDrawer shapeDrawer;

    private final List<Widget> children = new ArrayList<>();

    private float x;
    private float y;
    private float width;
    private float height;
    private float minWidth;
    private float minHeight;
    private float maxWidth = Float.MAX_VALUE;
    private float maxHeight = Float.MAX_VALUE;
    private boolean visible = true;

    private int align = Align.center;
    private Background background;

    private Widget parent;
    private boolean invalidated;

    public Widget() {
        invalidated = true;
    }

    public final void render(SpriteBatch batch) {
        if(invalidated)
            validate();

        if(!visible)
            return;

        if(background != null)
            background.draw(batch, this);

        draw(batch);
        children.forEach(widget -> widget.render(batch));
        update();
    }

    public void draw(SpriteBatch batch){}

    protected void validate() {
        invalidated = false;
        Log.trace("validating  " + toString());
        children.forEach(Widget::validate);
        layout();
        children.forEach(Widget::validate);
    }

    public void layout(){}

    public void update(){

    }

    public void handleClick(ClickEvent event){
        if(!visible)
            return;

        if(event.isOnWidget(this)){
            onClick(event);
        }

        children.forEach(child -> child.handleClick(event));
    }

    protected void onClick(ClickEvent event){}

    public <T extends Widget> T add(T widget){
        ((Widget) widget).parent = this; //Must cast here to get private field
        children.add(widget);

        return widget;
    }

    public Widget chainAdd(Widget widget){
        add(widget);

        return this;
    }

    public void add(Widget... widgets){
        var newChildren = Arrays.asList(widgets);

        newChildren.forEach(this::add);
    }

    public void invalidate(){
        Log.trace("Invalidated: " + getClass().getName());
        this.invalidated = true;
    }

    public void invalidateParent(){
        if(parent != null)
            parent.invalidate();
    }

    public void invalidateHierarchy(){
        invalidate();

        if(parent != null)
            parent.invalidateHierarchy();
    }

    public boolean isMouseOver() {
        var x = Gdx.input.getX();
        var y = Gdx.graphics.getHeight() - Gdx.input.getY();
        return x > getX() && y > getY() && x < getX() + getWidth() && y < getY() + getHeight();
    }

    public void onKeyEvent(KeyEvent keyEvent) {

    }

    public Widget setPosition(float x, float y){
        this.x = x;
        this.y = y;

        return this;
    }

    public Widget setVisible(boolean visible) {
        this.visible = visible;
        invalidateParent();

        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public Widget setSize(float width, float height){
        setWidth(width);
        setHeight(height);

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
        this.width = MathUtils.clamp(width * Scale.scale, minWidth, maxWidth);
        return this;
    }

    public Widget setHeight(float height) {
        this.height = MathUtils.clamp(height * Scale.scale, minHeight, maxHeight);

        return this;
    }

    public Widget setBackground(Background background) {
        this.background = background;
        return this;
    }

    public Widget setMinSize(float minWidth, float minHeight){
        setMinWidth(minWidth);
        setMinHeight(minHeight);

        return this;
    }

    public Widget setMinHeight(float minHeight) {
        this.minHeight = minHeight;
        this.height = MathUtils.clamp(height * Scale.scale, minHeight, maxHeight);

        return this;
    }

    public Widget setMinWidth(float minWidth) {
        this.minWidth = minWidth;
        this.width = MathUtils.clamp(width * Scale.scale, minWidth, maxWidth);

        return this;
    }

    public Widget setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
        this.width = MathUtils.clamp(width * Scale.scale, minWidth, maxWidth);

        return this;
    }

    public Widget setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
        this.height = MathUtils.clamp(height * Scale.scale, minHeight, maxHeight);

        return this;
    }

    public Padding pad(){
        return new Padding(this);
    }

    public float getMinHeight() {
        return minHeight;
    }

    public float getMinWidth() {
        return minWidth;
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

    public List<Widget> getChildren() {
        return children;
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
