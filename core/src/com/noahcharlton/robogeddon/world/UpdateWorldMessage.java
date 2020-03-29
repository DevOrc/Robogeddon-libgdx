package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.message.Message;

import java.util.List;

/**
 * Used by the server to send dirty tiles to the client
 */
public class UpdateWorldMessage implements Message {

    private final TileUpdate[] updates;

    public UpdateWorldMessage(List<Tile> dirtyTiles) {
        updates = new TileUpdate[dirtyTiles.size()];

        for(int i = 0; i < dirtyTiles.size(); i++) {
            updates[i] = new TileUpdate(dirtyTiles.get(i));
        }
    }

    public TileUpdate[] getUpdates() {
        return updates;
    }
}
