package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.floor.Floors;

public class Chunk {

    public static final int SIZE = 32;

    private final GridPoint2 location;
    private final Tile[][] tiles = new Tile[SIZE][SIZE];
    private boolean dirty;

    @Side(Side.SERVER)
    Chunk(World world, GridPoint2 location) {
        this.location = location;

        for(int x = 0; x < SIZE; x++){
            for(int y = 0; y < SIZE; y++) {
                var index = getArrayIndex(x, y);
                var worldX = x + (location.x * 32);
                var worldY = y + (location.y * 32);

                tiles[index.x][index.y] = new Tile(world,  this, worldX, worldY);
                tiles[index.x][index.y].setFloor(Floors.dirt, false);
            }
        }
    }

    @Side(Side.CLIENT)
    public Chunk(World world, WorldSyncMessage message) {
        this.location = message.getChunk();

        for(int x = 0; x < SIZE; x++){
            for(int y = 0; y < SIZE; y++) {
                var index = getArrayIndex(x, y);
                var worldX = x + (location.x * 32);
                var worldY = y + (location.y * 32);

                tiles[index.x][index.y] = new Tile(world,  this, worldX, worldY);
                tiles[index.x][index.y].update(message.getTiles()[index.x][index.y]);
            }
        }
    }

    /**
     * @returns the index the passed in tile should be placed on.
     * The indices are flipped on the negative side.
     *
     * Example: The tile (-1, 0) has the index 0 even though the origin of the chunk is at (-32, 0).
     * This is done so that the arrays are always going away from zero
     */
    private GridPoint2 getArrayIndex(int interX, int interY){
        GridPoint2 output = new GridPoint2();
        if(location.x >= 0){
            output.x = interX;
        }else{
            output.x = 31 - interX;
        }

        if(location.y >= 0){
            output.y = interY;
        }else{
            output.y = (31 - interY) % 32;
        }

         return output;
    }

    public void render(SpriteBatch batch){
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                getTile(x, y).render(batch);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= SIZE || y >= SIZE){
            return null;
        }

        return tiles[x][y];
    }

    public void markDirty(){
        dirty = true;
    }

    public void clean(){
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    public GridPoint2 getLocation() {
        return location;
    }
}
