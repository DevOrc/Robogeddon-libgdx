package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Multiblock;
import com.noahcharlton.robogeddon.block.tileentity.electricity.HasElectricity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntitySelectable;
import com.noahcharlton.robogeddon.util.Selectable;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.floor.Floor;

import java.util.Objects;

public class Tile implements Selectable, HasWorldPosition {

    public static final int SIZE = 32;

    private final World world;
    private final Chunk chunk;
    private final int x;
    private final int y;
    private final int pixelX;
    private final int pixelY;

    private Block block;
    private float blockHealth = 1f;
    private Floor floor;
    private Floor upperFloor;
    private TileEntity tileEntity;

    /**
     * On the server, if true, the tile is sent to client.
     * <p>
     * On the client, if true, the info is updated on the GUI
     */
    private boolean dirty;

    public Tile(World world, Chunk chunk, int x, int y) {
        this.x = x;
        this.y = y;
        this.pixelX = x * SIZE;
        this.pixelY = y * SIZE;
        this.chunk = chunk;
        this.world = world;
    }

    public void markDirty(){
        dirty = true;
        Log.trace("Marking " + toString() + " dirty");
    }

    @Side(Side.CLIENT)
    public void onTileUpdate(TileUpdate update) {
        markDirty(); //mark dirty on client for selectable

        if(update.floor == null){
            throw new RuntimeException("Cannot have null floor.");
        }else{
            floor = Core.floors.get(update.floor);
        }

        if(update.upperFloor != null){
            upperFloor = Core.floors.get(update.upperFloor);
        }else{
            upperFloor = null;
        }

        if(update.block == null){
            setBlock(null, false);
        }else if(update.block.startsWith("multi,")){
            var parts = update.block.substring(6).split(",", 3);
            var rootX = Integer.parseInt(parts[0]);
            var rootY = Integer.parseInt(parts[1]);
            var id = parts[2];

            setBlock(new Multiblock(Core.blocks.get(id), rootX, rootY), false);
        }else{
            setBlock(Core.blocks.get(update.block), false);
        }

        blockHealth = update.blockHealth;
    }

    @Side(Side.CLIENT)
    public void renderFloor(SpriteBatch batch) {
        if(floor != null)
            floor.render(batch, this);

        if(upperFloor != null)
            upperFloor.render(batch, this);
    }

    @Side(Side.CLIENT)
    public void renderBlock(SpriteBatch batch){
        if(hasBlock() && block.getRenderer() != null)
            block.getRenderer().render(batch, this);
    }

    public void setBlock(Block block, boolean markDirty) {
        if(markDirty)
            markDirty();

        this.block = block;
        this.blockHealth = 1f;
        if(block instanceof HasTileEntity){
            this.tileEntity = ((HasTileEntity) block).createTileEntity(this);
        }else{
            this.tileEntity = null;
        }
    }

    public void update(){
        if(tileEntity != null)
            tileEntity.update();

        if(blockHealth <= 0 && world.isServer()){
            ((ServerWorld) world).destroyBlock(this);
        }
    }

    public void setUpperFloor(Floor upperFloor, boolean markDirty) {
        if(markDirty)
            markDirty();

        this.upperFloor = upperFloor;
    }

    public void setFloor(Floor floor, boolean markDirty) {
        if(markDirty)
            markDirty();

        this.floor = Objects.requireNonNull(floor);
    }

    @Side(Side.SERVER)
    public void damage(){
        blockHealth -= .25 / block.getHardness();
        dirty = true;
    }

    @Side(Side.CLIENT)
    @Override
    public String getTitle() {
        return toString();
    }

    @Side(Side.CLIENT)
    @Override
    public String[] getDetails() {
        var defaultInfo = new String[]{
                "Block: " + (hasBlock() ? block.getDisplayName() : "None"),
                "Floor: " + floor.getDisplayName(),
                String.format("Health: %2.0f %%", blockHealth * 100),
         };

        return Selectable.combineArrays(defaultInfo, getTileEntityInfo());
    }

    private String[] getTileEntityInfo(){
        var tileEntity = getTileEntity();
        var inventory = tileEntity instanceof HasInventory ? ((HasInventory) tileEntity).getInventoryDetails()
                : new String[]{};
        var electricity = tileEntity instanceof HasElectricity ? ((HasElectricity) tileEntity).getElectricityDetails()
                : new String[]{};
        var details = tileEntity instanceof TileEntitySelectable ? ((TileEntitySelectable) tileEntity).getDetails()
                : new String[]{};

        return Selectable.combineArrays(details, Selectable.combineArrays(inventory, electricity));
    }

    @Override
    public String getDesc() {
        var tileEntity = getTileEntity();

        return tileEntity instanceof TileEntitySelectable ? ((TileEntitySelectable) tileEntity).getDesc() : "";
    }

    @Override
    public String getSubMenuID() {
        var tileEntity = getTileEntity();

        return tileEntity instanceof TileEntitySelectable ? ((TileEntitySelectable) tileEntity).getSubMenuID() : null;
    }

    @Side(Side.CLIENT)
    @Override
    public boolean isInfoInvalid() {
        return dirty;
    }

    @Side(Side.CLIENT)
    @Override
    public void onInfoValidated() {
        clean();
    }

    public void clean() {
        dirty = false;
    }

    public Tile[] getNeighbors(){
        var tiles = new Tile[4];

        tiles[0] = world.getTileAt(x - 1, y);
        tiles[1] = world.getTileAt(x + 1, y);
        tiles[2] = world.getTileAt(x, y - 1);
        tiles[3] = world.getTileAt(x, y + 1);

        return tiles;
    }

    public Tile[] getNeighborCorners(){
        var tiles = new Tile[4];

        tiles[0] = world.getTileAt(x - 1, y - 1);
        tiles[1] = world.getTileAt(x - 1, y + 1);
        tiles[2] = world.getTileAt(x + 1, y - 1);
        tiles[3] = world.getTileAt(x + 1, y + 1);

        return tiles;
    }

    public boolean isBlockOrMulti(Block block) {
        if(this.block == block)
            return true;

        if(this.block instanceof Multiblock){
            return ((Multiblock) this.block).getBlock() == block;
        }

        return false;
    }

    public float getBlockHealth() {
        return blockHealth;
    }

    public Block getBlock() {
        return block;
    }

    public boolean hasBlock(){
        return block != null;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPixelX() {
        return pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public Floor getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        return "Tile(" + x + ", " + y + ")";
    }

    public boolean isDirty() {
        return dirty;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public TileEntity getTileEntity() {
        if(block instanceof Multiblock){
            var block = ((Multiblock) this.block);
            var tile = world.getTileAt(block.getRootX(), block.getRootY());
            return tile == null ? null : tile.getTileEntity();
        }

        return tileEntity;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return getX() == tile.getX() &&
                getY() == tile.getY() &&
                getWorld().equals(tile.getWorld());
    }

    public Floor getUpperFloor() {
        return upperFloor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public float getWorldXPos() {
        return pixelX + (Tile.SIZE / 2f);
    }

    @Override
    public float getWorldYPos() {
        return pixelY + (Tile.SIZE / 2f);
    }
}
