package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.util.Side;

public class Tile {

    public static final int SIZE = 32;

    private final World world;
    private final int x;
    private final int y;


    /** Used by the server to tell which tiles should be sent to the client, after the next update*/
    @Side(Side.SERVER)
    private boolean dirty;

    private Block block;

    public Tile(World world, int x, int y) {
        this.x = x;
        this.y = y;
        this.world = world;
    }

    public void markDirty(){
        if(world.isClient())
            throw new UnsupportedOperationException();

        dirty = true;
        Log.debug("Marking " + toString() + " dirty");
    }

    @Side(Side.CLIENT)
    public void render(SpriteBatch batch) {
        if(hasBlock())
            block.getRenderer().render(batch, this);
    }

    public void setBlock(Block block, boolean markDirty) {
        if(markDirty)
            markDirty();

        this.block = block;
    }

    public void clean() {
        dirty = false;
    }

    public Block getBlock() {
        return block;
    }

    public boolean hasBlock(){
        return block != null;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPixelX(){
        return x * SIZE;
    }

    public int getPixelY(){
        return y * Tile.SIZE;
    }

    @Override
    public String toString() {
        return "Tile(" + x + ", " + y + ")";
    }

    public boolean isDirty() {
        return dirty;
    }
}
