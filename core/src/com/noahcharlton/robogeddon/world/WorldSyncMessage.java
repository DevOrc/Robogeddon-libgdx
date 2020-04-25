package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.team.Team;

public class WorldSyncMessage implements Message {

    private final GridPoint2 chunk;
    private final TileUpdate[][] tiles;
    private final Team team;

    public WorldSyncMessage(Chunk chunk) {
        this.chunk = chunk.getLocation().cpy();
        this.tiles = new TileUpdate[Chunk.SIZE][Chunk.SIZE];
        this.team = chunk.getTeam();

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

    public Team getTeam() {
        return team;
    }
}
