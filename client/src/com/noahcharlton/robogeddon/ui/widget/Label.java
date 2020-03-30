package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.ui.Scale;

import java.util.function.Supplier;

public class Label extends Widget {

    private Supplier<String> supplier = null;
    private BitmapFont font;
    private String text = "";
    private Color textColor = Color.WHITE;

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
        font.draw(batch, text, x, y);
    }

    @Override
    public void layout() {
        font.getData().setScale(Scale.scale);
        var textLayout = new GlyphLayout(font, text);

        setMinWidth(textLayout.width);
        setMinHeight(textLayout.height);
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

    public Label setText(String text) {
        invalidate();
        this.text = text;

        return this;
    }

    public String getText() {
        return text;
    }

}
