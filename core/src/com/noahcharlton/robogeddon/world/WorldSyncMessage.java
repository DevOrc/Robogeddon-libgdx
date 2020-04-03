package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.message.Message;

public class WorldSyncMessage implements Message {

    private final GridPoint2 chunk;
    private final TileUpdate[][] tiles;

    public WorldSyncMessage(Chunk chunk) {
        this.chunk = chunk.getLocation();
        this.tiles = new TileUpdate[Chunk.SIZE][Chunk.SIZE];

        for(int x = 0; x < Chunk.SIZE; x++){
            for(int y = 0; y < Chunk.SIZE; y++){
                var tile = chunk.getTile(x, y);

                tiles[x][y] = new TileUpdate(tile);
            }
        }
    }

    public TileUpdate[][] getTiles() {
        return tiles;
    }

    public GridPoint2 getChunk() {
        return chunk;
    }
}
