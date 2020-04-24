package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.floor.Floors;

import java.util.Random;

public class WorldGenerator {

    public static long seed = -4329852391L;

    private final Random random;
    private final SimplexNoise2D noiseMap;

    public WorldGenerator(long seed) {
        this.random = new Random();
        this.noiseMap = new SimplexNoise2D(random);
    }

    public void genChunksAround(ServerWorld world, Chunk chunk) {
        chunk.getNeighborLocations().stream().filter(pt -> world.getChunkAt(pt.x, pt.y) == null)
                .forEach(pt -> world.createChunk(pt.x, pt.y, true).setTeam(world.getEnemyTeam()));
    }

    public void createInitialWorld(ServerWorld world) {
        for(int x = -4; x <= 4; x++) {
            for(int y = -4; y <= 4; y++) {
                var chunk = world.createChunk(x, y, true);

                if(Math.abs(x) >= 3 || Math.abs(y) >= 3){
                    chunk.setTeam(world.getEnemyTeam());
                }else{
                    chunk.setTeam(world.getPlayerTeam());
                }
            }
        }
    }

    public void genChunk(Chunk chunk) {
        for(int x = 0; x < Chunk.SIZE; x++) {
            for(int y = 0; y < Chunk.SIZE; y++) {
                Tile tile = chunk.getTile(x, y);
                tile.setFloor(getFloorFor(tile), false);
            }
        }
    }

    private Floor getFloorFor(Tile tile){
        double noise = getNoise(tile);

        if(noise > .5) {
            if(noise > .7) {
                return Floors.ironRock;
            }else{
                return Floors.rock;
            }
        }

        var dirtVal = (Math.abs(noise) % .1) * 10;
        if(dirtVal < .2){
            return Floors.rockyDirt;
        }else if(dirtVal < .6) {
            return Floors.dirt1;
        }else{
            return Floors.dirt2;
        }
    }

    private double getNoise(Tile tile) {
        return getNoise(tile.getX(), tile.getY());
    }

    private double getNoise(int tileX, int tileY) {
        return noiseMap.noise(tileX / 16f, tileY / 16f);
    }
}
