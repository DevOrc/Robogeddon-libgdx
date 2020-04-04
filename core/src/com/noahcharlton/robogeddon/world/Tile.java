package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.floor.Floor;

import java.util.Objects;

public class Tile {

    public static final int SIZE = 32;

    private final World world;
    private final Chunk chunk;
    private final int x;
    private final int y;

    private Block block;
    private Floor floor;

    /** Used by the server to tell which tiles should be sent to the client, after the next update*/
    @Side(Side.SERVER)
    private boolean dirty;

    public Tile(World world, Chunk chunk, int x, int y) {
        this.x = x;
        this.y = y;
        this.chunk = chunk;
        this.world = world;
    }

    public void markDirty(){
        if(world.isClient())
            throw new UnsupportedOperationException();

        dirty = true;
        chunk.markDirty();
        Log.debug("Marking " + toString() + " dirty");
    }

    @Side(Side.CLIENT)
    public void update(TileUpdate update) {
        if(update.floor == null){
            throw new RuntimeException("Cannot have null floor.");
        }else{
            floor = Core.floors.get(update.floor);
        }

        if(update.block == null){
            block = null;
        }else{
            block = Core.blocks.get(update.block);
        }
    }

    @Side(Side.CLIENT)
    public void render(SpriteBatch batch) {
        if(floor != null)
            floor.render(batch, this);

        if(hasBlock())
            block.getRenderer().render(batch, this);
    }

    public void setBlock(Block block, boolean markDirty) {
        if(markDirty)
            markDirty();

        this.block = block;
    }

    public void setFloor(Floor floor, boolean markDirty) {
        if(markDirty)
            markDirty();

        this.floor = Objects.requireNonNull(floor);
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

    public Floor getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        return "Tile(" + x + ", " + y + ")";
    }

    public boolean isDirty() {
        return dirty;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
