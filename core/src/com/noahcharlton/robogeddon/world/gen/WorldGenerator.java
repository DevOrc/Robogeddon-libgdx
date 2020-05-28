package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;

import java.util.Random;

public class WorldGenerator {

    private final EnemyBaseGenerator baseGenerator;
    private final SimplexNoise2D noiseMap;
    private final SimplexNoise2D biomeMap;
    private final long seed;

    public WorldGenerator(long seed) {
        this.seed = seed;
        this.noiseMap = new SimplexNoise2D(new Random(seed));
        this.biomeMap = new SimplexNoise2D(new Random(seed * 2));
        this.baseGenerator = new EnemyBaseGenerator(seed * 3);
    }

    public void createInitialWorld(ServerWorld world) {
        for(int x = -4; x <= 4; x++) {
            for(int y = -4; y <= 4; y++) {
                var chunk = world.createChunk(x, y, true);

                if(Math.abs(x) >= 3 || Math.abs(y) >= 3) {
                    chunk.setTeam(world.getEnemyTeam());
                    baseGenerator.generate(chunk);;
                } else {
                    chunk.setTeam(world.getPlayerTeam());
                }
            }
        }

        world.buildBlock(world.getTileAt(-1, -1), Blocks.inventoryPortal);
        world.getUnlockedBlocks().add(Blocks.minerBlock, Blocks.itemDuctNorth, Blocks.blueBeacon, Blocks.wall);
//        world.addEntity(EntityType.droneEntity.create(world, world.getEnemyTeam()));
    }

    public void genChunk(Chunk chunk) {
        for(int x = 0; x < Chunk.SIZE; x++) {
            for(int y = 0; y < Chunk.SIZE; y++) {
                var tile = chunk.getTile(x, y);
                var biomeVal = biomeMap.noise(tile.getX() / 128f, tile.getY() / 128f);
                var biome = Biomes.get(biomeVal);

                biome.gen(tile, getGenericNoise(tile));
            }
        }
    }

    public void genChunksAround(ServerWorld world, Chunk chunk) {
        chunk.getNeighborLocations().stream().filter(pt -> world.getChunkAt(pt.x, pt.y) == null).forEach(pt -> {
            var createdChunk = world.createChunk(pt.x, pt.y, true);
            createdChunk.setTeam(world.getEnemyTeam());

            baseGenerator.generate(chunk);
        });
    }

    private double getGenericNoise(Tile tile) {
        return noiseMap.noise(tile.getX() / 16f, tile.getY() / 16f);
    }

    public long getSeed() {
        return seed;
    }

    public EnemyBaseGenerator getBaseGenerator() {
        return baseGenerator;
    }
}
