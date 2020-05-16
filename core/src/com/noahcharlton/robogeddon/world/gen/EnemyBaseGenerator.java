package com.noahcharlton.robogeddon.world.gen;

import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.duct.ItemDuct;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.IntermediateDirection;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;

import java.util.Random;

public class EnemyBaseGenerator {

    private final SimplexNoise2D noise;

    EnemyBaseGenerator(long seed) {
        noise = new SimplexNoise2D(new Random(seed));
    }

    void generate(Chunk chunk) {
        // When main menu world is created, the assets haven't been loaded, so there are no bases to generate from
        if(BaseComponentType.POWER.getComponents().size() == 0)
            return;

        Random random = getChunkNoise(chunk);

        if(random.nextInt(5) == 0){
            generate(chunk, random);
        }
    }

    private void generate(Chunk chunk, Random random) {
        BaseComponent[] components = new BaseComponent[4];

        addComponent(random, components, BaseComponentType.POWER);
        addComponent(random, components, BaseComponentType.ARTILLERY);
        addComponent(random, components, BaseComponentType.ARTILLERY);
        addComponent(random, components, BaseComponentType.MISC);

        for(int i = 0; i < 4; i++){
            applyComponent(IntermediateDirection.values()[i], components[i], chunk);
        }
    }

    private void addComponent(Random random, BaseComponent[] components, BaseComponentType type) {
        var typeCount = type.getComponents().size();
        var value = type.getComponents().get(random.nextInt(typeCount));

        for(var i = random.nextInt(4);; i = random.nextInt(4)){
            if(components[i] == null){
                components[i] = value;
                return;
            }
        }
    }

    private Random getChunkNoise(Chunk chunk){
        var tile = chunk.getTile(0, 0);
        var seed = noise.noise(tile.getX(), tile.getY()) * 1_000_000_000;

        return new Random((long) seed);
    }

    public void applyComponent(IntermediateDirection direction, BaseComponent component, Chunk chunk) {
        ServerWorld world = (ServerWorld) chunk.getWorld();
        var rootPos = getRootPosition(chunk);

        for(var instruction : component.getInstructions()){
            var instructionPos = getInstructionPos(instruction.x, instruction.y, rootPos, direction, instruction.block);
            var tile = world.getTileAt(instructionPos.x, instructionPos.y);

            if(instruction.floor != null)
                tile.setFloor(instruction.floor, false);

            if(instruction.upperFloor != null)
                tile.setUpperFloor(instruction.upperFloor, false);

            if(instruction.block != null){
                if(instruction.block instanceof ItemDuct){
                    buildItemDuct((ItemDuct) instruction.block, direction, tile);
                }else{
                    world.buildBlock(tile, instruction.block);
                }
            }
        }
    }

    private void buildItemDuct(ItemDuct block, IntermediateDirection direction, Tile tile) {
        if(flipX(direction)){
            if(block.getDirection() == Direction.WEST){
                block = (ItemDuct) Blocks.itemDuctEast;
            }else if(block.getDirection() == Direction.EAST){
                block = (ItemDuct) Blocks.itemDuctWest;
            }
        }

        if(flipY(direction)){
            if(block.getDirection() == Direction.NORTH){
                block = (ItemDuct) Blocks.itemDuctSouth;
            }else if(block.getDirection() == Direction.SOUTH){
                block = (ItemDuct) Blocks.itemDuctNorth;
            }
        }

        tile.setBlock(block, false);
    }

    private GridPoint2 getInstructionPos(int relX, int relY, GridPoint2 rootPos, IntermediateDirection direction,
                                         Block block) {
        if(flipX(direction)){
            relX *= -1;
            relX -= block == null ? 1 : block.getWidth();
        }

        if(flipY(direction)){
            relY *= -1;
            relY -= block == null ? 1 : block.getHeight();
        }

        relX += rootPos.x;
        relY += rootPos.y;
        return new GridPoint2(relX, relY);
    }

    public GridPoint2 getRootPosition(Chunk chunk) {
        int x = (chunk.getLocation().x * Chunk.SIZE) + Chunk.SIZE / 2;
        int y = (chunk.getLocation().y * Chunk.SIZE) + Chunk.SIZE / 2;

        return new GridPoint2(x, y);
    }

    private boolean flipX(IntermediateDirection direction){
        return direction == IntermediateDirection.SOUTHWEST || direction == IntermediateDirection.NORTHWEST;
    }

    private boolean flipY(IntermediateDirection direction){
        return direction == IntermediateDirection.SOUTHWEST || direction == IntermediateDirection.SOUTHEAST;
    }
}
