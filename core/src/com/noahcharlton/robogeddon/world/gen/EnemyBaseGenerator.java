package com.noahcharlton.robogeddon.world.gen;

import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.block.Block;
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

    }

    public void applyComponent(IntermediateDirection direction, BaseComponent component, Chunk chunk) {
        ServerWorld world = (ServerWorld) chunk.getWorld();
        var rootPos = getRootPosition(chunk);

        for(var instruction : component.getInstructions()){
            var instructionPos = getInstructionPos(instruction.x, instruction.y, rootPos, direction, instruction.block);
            var tile = world.getTileAt(instructionPos.x, instructionPos.y);

            executeInstruction(world, instruction, tile);
        }
    }

    private void executeInstruction(ServerWorld world, BaseComponent.ComponentInstruction instruction, Tile tile) {
        if(instruction.floor != null)
            tile.setFloor(instruction.floor, false);

        if(instruction.upperFloor != null)
            tile.setUpperFloor(instruction.upperFloor, false);

        if(instruction.block != null)
            world.buildBlock(tile, instruction.block);
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
