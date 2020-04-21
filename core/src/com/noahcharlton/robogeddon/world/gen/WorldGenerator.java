package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.floor.Floors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

public class WorldGenerator {

    public static long seed = -4329852391L;

    private final Random random;
    private Chunk currentChunk;

    public WorldGenerator(long seed) {
        this.random = new Random(seed);
    }

    public void genChunk(Chunk chunk) {
        currentChunk = chunk;

        for(int i = 0; i < 3; i++){
            int x = random.nextInt(Chunk.SIZE);
            int y = random.nextInt(Chunk.SIZE);
            generateFeature(x, y, 5 + (int) (45 * random.nextDouble()), this::setOil);
        }
        fixPatches(Floors.oil_rock, this::setOil);
        surroundFloor(Floors.oil_rock, Floors.sand);
    }

    private void setOil(Tile tile) {
        tile.setFloor(Floors.oil_rock, false);
        tile.setBlock(Blocks.oilBlock, false);
    }

    private void generateFeature(int startX, int startY, int i, Consumer<Tile> feature) {
        for(int x = startX - 1; x <= startX + 1; x++){
            for(int y = startY - 1; y <= startY + 1; y++) {
                var tile = currentChunk.getTile(x, y);

                if(tile != null)
                    feature.accept(tile);
            }
        }

        if(i > 0){
            startX += random.nextBoolean() ? 1 : -1;
            startY += random.nextBoolean() ? 1 : -1;
            generateFeature(startX, startY, i  -  1, feature);
        }
    }

    private void fixPatches(Floor type, Consumer<Tile> consumer){
        for(int x = 1; x < Chunk.SIZE; x++) {
            for(int y = 1; y < Chunk.SIZE; y++) {
                Tile tile = currentChunk.getTile(x, y);

                if(tile.getFloor() != type){
                    List<Tile> neighbors = getNeighborTiles(x, y);

                    int floorCount = (int) neighbors.stream().filter(t -> t.getFloor() == type).count();

                    if(floorCount == 4){
                        consumer.accept(tile);
                    }
                }
            }
        }
    }

    private void surroundFloor(Floor main, Floor border) {
        for(int x = 1; x < Chunk.SIZE; x++) {
            for(int y = 1; y < Chunk.SIZE; y++) {
                Tile tile = currentChunk.getTile(x, y);

                if(Floors.isDirt(tile.getFloor())){
                    List<Tile> neighbors = getNeighborTiles(x, y);

                    int floorCount = (int) neighbors.stream().filter(t -> t.getFloor() == main).count();

                    if(floorCount > 0 && floorCount < 4){
                        tile.setFloor(border, false);
                    }
                }
            }
        }
    }

    private List<Tile> getNeighborTiles(int x, int y) {
        List<Tile> neighbors = new ArrayList<>();
        neighbors.add(currentChunk.getTile(x - 1, y));
        neighbors.add(currentChunk.getTile(x + 1, y));
        neighbors.add(currentChunk.getTile(x, y - 1));
        neighbors.add(currentChunk.getTile(x, y + 1));
        neighbors.removeIf(Objects::isNull);
        return neighbors;
    }

}
