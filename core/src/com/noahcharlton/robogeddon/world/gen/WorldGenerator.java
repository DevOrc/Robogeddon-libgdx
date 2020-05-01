package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.floor.Floors;

import java.util.Random;

public class WorldGenerator {

    private final SimplexNoise2D noiseMap;
    private final long seed;

    public WorldGenerator(long seed) {
        this.seed = seed;
        this.noiseMap = new SimplexNoise2D(new Random(seed));
    }

    public void genChunksAround(ServerWorld world, Chunk chunk) {
        chunk.getNeighborLocations().stream().filter(pt -> world.getChunkAt(pt.x, pt.y) == null).forEach(pt -> {
            var createdChunk = world.createChunk(pt.x, pt.y, true);
            createdChunk.setTeam(world.getEnemyTeam());
            generateEnemyStructures(createdChunk);
        });
    }

    private void generateEnemyStructures(Chunk chunk) {
        for(int x = 4; x < Chunk.SIZE - 4; x++) {
            for(int y = 4; y < Chunk.SIZE - 4; y++) {
                Tile tile = chunk.getTile(x, y);

                if(tile != null){
                    double noise = getNoise(tile);

                    if(noise < -.25 && noise > -.251){
                        spawnTurret(tile);

                        x += 4;
                        y += 4;
                    }
                }
            }
        }
    }

    private void spawnTurret(Tile tile) {
        ServerWorld world = (ServerWorld) tile.getWorld();
        int tileX = tile.getX();
        int tileY = tile.getY();

        for (int x = tileX - 2; x <= tileX + 2; x++){
            for (int y = tileY - 2; y <= tileY + 2; y++){
                var wallTile = world.getTileAt(x, y);

                if(wallTile != null)
                    wallTile.setBlock(Blocks.wall, false);
            }
        }
        Tile metalFormerTile = world.getTileAt(tileX - 1, tileY - 1);
        world.buildBlock(metalFormerTile, Blocks.metalFormer);
        world.getTileAt(tileX, tileY + 1).setBlock(Blocks.itemDuctSouth, false);
        world.getTileAt(tileX + 1, tileY - 1).setBlock(Blocks.itemDuctNorth, false);
        world.getTileAt(tileX + 1, tileY).setBlock(Blocks.turretBlock, false);


        setMiner(tileX - 1, tileY + 1, world);
        setMiner(tileX + 1, tileY + 1, world);
    }

    private void setMiner(int tileX, int tileY, ServerWorld world) {
        world.getTileAt(tileX, tileY).setFloor(Floors.ironRock, false);
        world.getTileAt(tileX, tileY).setBlock(Blocks.minerBlock, false);
    }

    public void createInitialWorld(ServerWorld world) {
        for(int x = -4; x <= 4; x++) {
            for(int y = -4; y <= 4; y++) {
                var chunk = world.createChunk(x, y, true);

                if(Math.abs(x) >= 3 || Math.abs(y) >= 3) {
                    chunk.setTeam(world.getEnemyTeam());
                    generateEnemyStructures(chunk);
                } else {
                    chunk.setTeam(world.getPlayerTeam());
                }
            }
        }

        world.buildBlock(world.getTileAt(-1, -1), Blocks.inventoryPortal);
//        world.addEntity(EntityType.droneEntity.create(world, world.getEnemyTeam()));
    }

    public void genChunk(Chunk chunk) {
        for(int x = 0; x < Chunk.SIZE; x++) {
            for(int y = 0; y < Chunk.SIZE; y++) {
                Tile tile = chunk.getTile(x, y);
                tile.setFloor(getFloorFor(tile), false);
            }
        }
    }

    private Floor getFloorFor(Tile tile) {
        double noise = getNoise(tile);

        if(noise > .5) {
            if(noise > .7) {
                return Floors.ironRock;
            } else {
                return Floors.rock;
            }
        }

        var dirtVal = (Math.abs(noise) % .1) * 10;
        if(dirtVal < .2) {
            return Floors.rockyDirt;
        } else if(dirtVal < .6) {
            return Floors.dirt1;
        } else {
            return Floors.dirt2;
        }
    }

    private double getNoise(Tile tile) {
        return getNoise(tile.getX(), tile.getY());
    }

    private double getNoise(int tileX, int tileY) {
        return noiseMap.noise(tileX / 16f, tileY / 16f);
    }

    public long getSeed() {
        return seed;
    }
}
