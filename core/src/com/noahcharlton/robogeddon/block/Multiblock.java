package com.noahcharlton.robogeddon.block;

/**
 * Should NOT BE registered with the core -
 * simply used for storing a multiblock in the world class!
 */
public final class Multiblock extends Block{

    private final Block block;
    private final int rootX;
    private final int rootY;

    public Multiblock(Block block, int rootX, int rootY) {
        super("multi," + rootX +"," + rootY + "," + block.getTypeID());

        this.block = block;
        this.rootX = rootX;
        this.rootY = rootY;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public String getDisplayName() {
        return block.getDisplayName();
    }

    @Override
    public BlockRenderer getRenderer() {
        return null;
    }

    public int getRootX() {
        return rootX;
    }

    public int getRootY() {
        return rootY;
    }

    @Override
    public String toString() {
        return "Multiblock{" +
                "block=" + block +
                ", rootX=" + rootX +
                ", rootY=" + rootY +
                '}';
    }
}
