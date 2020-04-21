package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scale;
import com.noahcharlton.robogeddon.ui.UIAssets;

import java.util.function.Supplier;

public class Label extends NoChildrenWidget {

    private Supplier<String> supplier = null;
    private BitmapFont font = UIAssets.smallFont;
    private String text = "";
    private Color textColor = Color.WHITE;
    private boolean wrap;

    public Label() {
    }

    public Label(String text) {
        this.text = text;
    }

    @Override
    public void update() {
        if(supplier != null){
            text = supplier.get();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        var x = getX();
        var y = getY() + getHeight();

        font.getData().setScale(Scale.scale);
        font.setColor(Color.WHITE);
        font.draw(batch, text, x, y, getWidth(), Align.center, wrap);
    }

    @Override
    public void layout() {
        font.getData().setScale(Scale.scale);
        var textLayout = new GlyphLayout(font, text);

        setMinWidth(textLayout.width);
        setMinHeight(textLayout.height);
    }

    public void pack(){
        setWidth(getMinWidth());
        setHeight(getMinHeight());
    }

    public Label setSupplier(Supplier<String> supplier) {
        this.supplier = supplier;
        return this;
    }

    public Label setTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Label setFont(BitmapFont font) {
        invalidate();
        this.font = font;
        return this;
    }

    public BitmapFont getFont() {
        return font;
    }

    @Override
    public String toString() {
        return "Label(" + text + ")";
    }

    public Label setWrap(boolean wrap) {
        this.wrap = wrap;
        invalidate();

        return this;
    }

    public Label setText(String text) {
        invalidate();
        this.text = text;

        return this;
    }

    public String getText() {
        return text;
    }

}
