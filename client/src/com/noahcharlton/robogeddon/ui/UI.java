package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.client.GameClient;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.item.Item;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class UI {

    private final ScreenViewport screenViewport = new ScreenViewport();
    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeDrawer drawer;

    private final GameClient client;
    private final BitmapFont font = new BitmapFont();

    public UI(GameClient client) {
        this.client = client;

        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGB565);

        map.setColor(Color.WHITE);
        map.drawPixel(0, 0);

        TextureRegion dot = new TextureRegion(new Texture(map));
        drawer = new ShapeDrawer(batch, dot);
    }

    public void render(World world){
        font.setColor(Color.RED);

        var matrix = screenViewport.getCamera().projection;
        matrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(matrix);
        batch.begin();

        int y = Gdx.graphics.getHeight() - 10;
        int x = 10;

        for(Item item: Core.items.values()){
            var text = item.getTypeID() + ": " + world.getInventoryForItem(item);
            font.draw(batch, text, x, y);

            y -= 20;
        }

        batch.end();
    }

    public void resize(int width, int height){
        screenViewport.update(width, height);
    }
}
