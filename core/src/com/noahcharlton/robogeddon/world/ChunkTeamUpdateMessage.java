package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.team.Team;

public class ChunkTeamUpdateMessage implements Message {

    private int chunkX;
    private int chunkY;
    private Team newTeam;

    public ChunkTeamUpdateMessage(Chunk chunk) {
        this.chunkX = chunk.getLocation().x;
        this.chunkY = chunk.getLocation().y;
        this.newTeam = chunk.getTeam();
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public Team getNewTeam() {
        return newTeam;
    }
}
