package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.io.Element;

import java.util.ArrayList;
import java.util.List;

public class BaseComponent {

    private final String name;
    private final BaseComponentType type;
    private final List<ComponentInstruction> instructions = new ArrayList<>();

    BaseComponent(BaseComponentType type, String name) {
        this.type = type;
        this.name = name;
    }

    void addInstruction(int x, int y, Block block, Floor floor, Floor upperFloor, Element tileEntityData){
        instructions.add(new ComponentInstruction(block, floor, upperFloor, x, y, tileEntityData));
    }

    static class ComponentInstruction {

        final Block block;
        final Floor floor;
        final Floor upperFloor;
        final Element tileEntityData;
        final int x;
        final int y;

        ComponentInstruction(Block block, Floor floor, Floor upperFloor, int x, int y, Element tileEntityData) {
            this.block = block;
            this.floor = floor;
            this.upperFloor = upperFloor;
            this.x = x;
            this.y = y;
            this.tileEntityData = tileEntityData;
        }
    }

    List<ComponentInstruction> getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public BaseComponentType getType() {
        return type;
    }
}
