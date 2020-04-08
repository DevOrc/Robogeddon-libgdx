package com.noahcharlton.robogeddon.block.duct;

import com.noahcharlton.robogeddon.block.tileentity.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class ItemDuctTileEntity extends TileEntity implements HasInventory{

    private final Direction direction;

    private boolean connectNorth;
    private boolean connectSouth;
    private boolean connectEast;
    private boolean connectWest;

    public ItemDuctTileEntity(Tile rootTile, Direction direction) {
        super(rootTile);

        this.direction = direction;
    }

    @Override
    public boolean acceptItem(Item item) {
        return false;
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {

    }

    @Override
    public Item retrieveItem() {
        return null;
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[0];
    }

    @Override
    public boolean canConnect(Direction from, Direction beltDirection) {
        boolean fromDir = from == Direction.NORTH || from == Direction.SOUTH;
        boolean ourDir = direction == Direction.NORTH || direction == Direction.SOUTH;
        boolean beltDir = beltDirection == Direction.NORTH || beltDirection == Direction.SOUTH;

        return ourDir == fromDir || fromDir == beltDir;
    }

    @Override
    public void update() {
        if(world.isServer())
            updateConnections();
    }

    private void updateConnections() {
        var tileX = getRootTile().getX();
        var tileY = getRootTile().getY();

        var connectNorth = hasInventory(tileX, tileY + 1, Direction.SOUTH);
        var connectEast = hasInventory(tileX + 1, tileY, Direction.WEST);
        var connectSouth = hasInventory(tileX, tileY - 1, Direction.NORTH);
        var connectWest = hasInventory(tileX - 1, tileY, Direction.WEST);

        if(connectNorth != this.connectNorth){
            this.connectNorth = connectNorth;
            dirty = true;
        }
        if(connectEast != this.connectEast){
            this.connectEast = connectEast;
            dirty = true;
        }
        if(connectSouth != this.connectSouth){
            this.connectSouth = connectSouth;
            dirty = true;
        }
        if(connectWest != this.connectWest){
            this.connectWest = connectWest;
            dirty = true;
        }
    }

    private boolean hasInventory(int tileX, int tileY, Direction from){
        var tile = world.getTileAt(tileX, tileY);

        return tile.getTileEntity() != null && tile.getTileEntity() instanceof HasInventory
                && ((HasInventory) tile.getTileEntity()).canConnect(from, direction);
    }

    @Override
    public void receiveData(float[] data) {
        if(data.length != 4)
            return;

        connectNorth = floatToBoolean(data[0]);
        connectEast = floatToBoolean(data[1]);
        connectSouth = floatToBoolean(data[2]);
        connectWest = floatToBoolean(data[3]);
    }

    @Override
    public float[] getData() {
        return new float[]{
                booleanToFloat(connectNorth),
                booleanToFloat(connectEast),
                booleanToFloat(connectSouth),
                booleanToFloat(connectWest),
        };
    }

    private boolean floatToBoolean(float data) {
        return data == 1;
    }

    private static float booleanToFloat(boolean connectNorth) {
        return connectNorth ? 1 : 0;
    }

    public boolean isConnectEast() {
        return connectEast;
    }

    public boolean isConnectNorth() {
        return connectNorth;
    }

    public boolean isConnectSouth() {
        return connectSouth;
    }

    public boolean isConnectWest() {
        return connectWest;
    }
}
