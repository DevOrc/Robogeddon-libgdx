package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.floor.Floors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class WorldGenerator {

    private static long seed = -432985239;

    private final Random random;
    private final ServerWorld world;

    public WorldGenerator(ServerWorld world, long seed) {
        this.world = world;
        this.random = new Random(seed);
    }

    public void gen(){
        for(int i = 0; i < 20; i++){
            int x = random.nextInt(world.getWidth());
            int y = random.nextInt(world.getHeight());
            generateFeature(x, y, 5 + (int) (45 * random.nextDouble()), this::setOil);
        }
        fixPatches(Floors.oil_rock, this::setOil);
        surroundFloor(Floors.oil_rock, Floors.sand);

        world.addEntity(EntityType.droneEntity.create(world));
    }

    private void setOil(Tile tile) {
        tile.setFloor(Floors.oil_rock, false);
        tile.setBlock(Blocks.oilBlock, false);
    }

    private void generateFeature(int startX, int startY, int i, Consumer<Tile> feature) {
        for(int x = startX - 1; x <= startX + 1; x++){
            for(int y = startY - 1; y <= startY + 1; y++) {
                var tile = world.getTileAt(x, y);

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
        for(int x = 1; x < world.getWidth() - 1; x++) {
            for(int y = 1; y < world.getHeight() - 1; y++) {
                Tile tile = world.getTileAt(x, y);

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
        for(int x = 1; x < world.getWidth() - 1; x++) {
            for(int y = 1; y < world.getHeight() - 1; y++) {
                Tile tile = world.getTileAt(x, y);

                if(tile.getFloor() == Floors.dirt){
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
        neighbors.add(world.getTileAt(x - 1, y));
        neighbors.add(world.getTileAt(x + 1, y));
        neighbors.add(world.getTileAt(x, y - 1));
        neighbors.add(world.getTileAt(x, y + 1));
        return neighbors;
    }


    public static void gen(ServerWorld world){
        new WorldGenerator(world, seed).gen();
    }

}
