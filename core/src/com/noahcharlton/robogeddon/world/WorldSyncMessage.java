package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.message.Message;

/**
 * Sends the width, height, and one y-level of tiles to the client.
 * It only sends one y-level because of the message byte limit (2^16)
 */
public class WorldSyncMessage implements Message {

    private final TileUpdate[] tiles;
    private final int worldWidth;
    private final int worldHeight;
    private final int yLevel;

    public WorldSyncMessage(World world, int yLevel) {
        if(yLevel >= world.getHeight())
            throw new IllegalArgumentException("Y-level must be less than world height!");

        this.tiles = new TileUpdate[world.getWidth()];
        this.worldWidth = world.getWidth();
        this.worldHeight = world.getHeight();
        this.yLevel = yLevel;

        for(int x = 0; x < world.getWidth(); x++){
            var tile = world.getTileAt(x, yLevel);

            tiles[x] = new TileUpdate(tile);
        }
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public TileUpdate[] getTiles() {
        return tiles;
    }

    public int getYLevel() {
        return yLevel;
    }
}
