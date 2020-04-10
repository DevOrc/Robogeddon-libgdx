package com.noahcharlton.robogeddon.block.duct;

import com.badlogic.gdx.math.MathUtils;
import com.noahcharlton.robogeddon.block.tileentity.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class ItemDuctTileEntity extends TileEntity implements HasInventory {

    private static final int SPEED = 1;
    private final Direction direction;

    private ItemBuffer[] buffers;
    private float[] itemXs;
    private float[] itemYs;

    private boolean connectNorth;
    private boolean connectSouth;
    private boolean connectEast;
    private boolean connectWest;

    public ItemDuctTileEntity(Tile rootTile, Direction direction) {
        super(rootTile);

        this.direction = direction;
        this.itemXs = new float[3];
        this.itemYs = new float[3];
        this.buffers = new ItemBuffer[]{
                new GenericItemBuffer(1),
                new GenericItemBuffer(1),
                new GenericItemBuffer(1)
        };
    }

    @Override
    public void update() {
        if(world.isServer()) {
            updateConnections();
            pullFromConnections();
            pushItems();
        }

        updateItemPositions();
    }

    private void pushItems() {
        for(int i = 0; i < itemXs.length; i++) {
            if(shouldBePushed(i)){
                pushBuffer(i);
            }
        }
    }

    private boolean shouldBePushed(int i) {
        if(direction == Direction.NORTH){
            return itemYs[i] == Tile.SIZE && connectNorth && buffers[i].getAmount() >= 1;
        }else if(direction == Direction.SOUTH){
            return itemYs[i] == 0 && connectSouth && buffers[i].getAmount() >= 1;
        }else if(direction == Direction.EAST){
            return itemXs[i] == Tile.SIZE && connectEast && buffers[i].getAmount() >= 1;
        }else{
            return itemXs[i] == 0 && connectWest && buffers[i].getAmount() >= 1;
        }
    }

    private void pushBuffer(int item) {
        int tileX = getRootTile().getX() + direction.getDeltaX();
        int tileY = getRootTile().getY() + direction.getDeltaY();
        var tileEntity = world.getTileAt(tileX, tileY).getTileEntity();

        if(tileEntity instanceof ItemDuctTileEntity){
            var itemDuct = (ItemDuctTileEntity) tileEntity;
            var buffer = buffers[item];

            if(itemDuct.acceptItem(buffer.currentItem(), getRootTile().getX(), getRootTile().getY())){
                buffer.retrieveItem();
                dirty = true;
            }
        }
    }

    private void updateItemPositions() {
        var deltaX = 0;
        var deltaY = 0;

        if(direction == Direction.NORTH)
            deltaY = SPEED;
        else if(direction == Direction.SOUTH)
            deltaY = -SPEED;
        else if(direction == Direction.EAST)
            deltaX = SPEED;
        else
            deltaX = -SPEED;

        for(int i = 0; i < itemXs.length; i++) {
            if(isBlocked(i)){
            }else if(!isInCenter(i)){
                moveToCenter(i);
            }else{
                itemXs[i] = MathUtils.clamp(itemXs[i]  + deltaX, getMinXPos(), getMaxXPos());
                itemYs[i] = MathUtils.clamp(itemYs[i]  + deltaY, getMinYPos(), getMaxYPos());
            }
        }
    }

    private boolean isBlocked(int checkIndex) {
        var checkX = itemXs[checkIndex];
        var checkY = itemYs[checkIndex];
        var radius = (2 * Item.ICON_RADIUS) + calculateItemSpacing();

        for(int i = 0; i < itemXs.length; i++) {
            if(i == checkIndex || buffers[i].getAmount() == 0){
                continue;
            }

            var x = itemXs[i];
            var y = itemYs[i];

            var distance = Math.sqrt(Math.pow(checkX - x, 2) + Math.pow(checkY - y, 2));

            if(radius > distance && inFrontOf(checkIndex, i)){
                return true;
            }
        }

        return false;
    }

    private boolean inFrontOf(int backIndex, int frontIndex) {
        if(direction == Direction.NORTH) {
            return itemYs[frontIndex] > itemYs[backIndex];
        }else if(direction == Direction.SOUTH){
            return itemYs[frontIndex] < itemYs[backIndex];
        }else if(direction == Direction.EAST){
            return itemXs[frontIndex] > itemXs[backIndex];
        }else{
            return itemXs[frontIndex] < itemXs[backIndex];
        }
    }

    private float calculateItemSpacing() {
        float size;
        if(direction.isNorthSouth()){
            size = getMaxYPos() - getMinYPos();
        }else{
            size = getMaxXPos() - getMinXPos();
        }

        if(size > 30)
            return 1.1f;
        return 0;
    }

    private boolean isInCenter(int i) {
        if(direction.isNorthSouth()){
            return itemXs[i] == Tile.SIZE / 2f;
        }else{
            return itemYs[i] == Tile.SIZE / 2f;
        }
    }

    private void moveToCenter(int i) {
        if(direction.isNorthSouth()){
            float error = 16 - itemXs[i];
            itemXs[i] += SPEED * Math.signum(error);
        }else{
            float error = 16 - itemYs[i];
            itemYs[i] += SPEED * Math.signum(error);
        }
    }

    private void pullFromConnections() {
        var tileX = getRootTile().getX();
        var tileY = getRootTile().getY();

        if(connectNorth) {
            pullFrom(tileX, tileY + 1);
        }
        if(connectSouth) {
            pullFrom(tileX, tileY - 1);
        }
        if(connectEast) {
            pullFrom(tileX + 1, tileY);
        }
        if(connectWest) {
            pullFrom(tileX - 1, tileY);
        }
    }

    private void pullFrom(int x, int y) {
        var inventory = (HasInventory) world.getTileAt(x, y).getTileEntity();
        Item item;

        if((item = inventory.retrieveItem(true)) != null) {
            if(acceptItem(item, x, y)) {
                inventory.retrieveItem(false);
                dirty = true;
            }
        }
    }

    private boolean acceptItem(Item item, int tileX, int tileY){
        for(int i = 0; i < buffers.length; i++) {
            var buffer = buffers[i];

            if(buffer.acceptItem(item)) {
                dirty = true;
                setItemPosition(i, tileX, tileY);
                return true;
            }
        }

        return false;
    }

    private void setItemPosition(int i, int tileX, int tileY) {
        if(tileX < getRootTile().getX()){
            itemXs[i] = 0;
        }else if(tileX > getRootTile().getX()){
            itemXs[i] = 32;
        }else{
            itemXs[i] = 16;
        }

        if(tileY < getRootTile().getY()){
            itemYs[i] = 0;
        }else if(tileY > getRootTile().getY()){
            itemYs[i] = 32;
        }else{
            itemYs[i] = 16;
        }
    }

    @Override
    public boolean acceptItem(Item item) {
        //The internal copy should be used instead
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        this.buffers = buffers;
    }

    @Override
    public Item retrieveItem(boolean simulate) {
        //Items are never retrieved by item ducts,
        //they are always pushed into the other block
        return null;
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return buffers;
    }

    @Override
    public boolean canConnect(Direction from, Direction beltDirection) {
        boolean fromDir = from == Direction.NORTH || from == Direction.SOUTH;
        boolean ourDir = direction == Direction.NORTH || direction == Direction.SOUTH;
        boolean beltDir = beltDirection == Direction.NORTH || beltDirection == Direction.SOUTH;

        return ourDir == fromDir || fromDir == beltDir;
    }

    private void updateConnections() {
        var tileX = getRootTile().getX();
        var tileY = getRootTile().getY();

        var connectNorth = hasInventory(tileX, tileY + 1, Direction.SOUTH);
        var connectEast = hasInventory(tileX + 1, tileY, Direction.WEST);
        var connectSouth = hasInventory(tileX, tileY - 1, Direction.NORTH);
        var connectWest = hasInventory(tileX - 1, tileY, Direction.WEST);

        if(connectNorth != this.connectNorth) {
            this.connectNorth = connectNorth;
            dirty = true;
        }
        if(connectEast != this.connectEast) {
            this.connectEast = connectEast;
            dirty = true;
        }
        if(connectSouth != this.connectSouth) {
            this.connectSouth = connectSouth;
            dirty = true;
        }
        if(connectWest != this.connectWest) {
            this.connectWest = connectWest;
            dirty = true;
        }
    }

    private boolean hasInventory(int tileX, int tileY, Direction from) {
        var tile = world.getTileAt(tileX, tileY);

        return tile.getTileEntity() != null && tile.getTileEntity() instanceof HasInventory
                && ((HasInventory) tile.getTileEntity()).canConnect(from, direction);
    }

    @Override
    public void receiveData(float[] data) {
        connectNorth = floatToBoolean(data[0]);
        connectEast = floatToBoolean(data[1]);
        connectSouth = floatToBoolean(data[2]);
        connectWest = floatToBoolean(data[3]);

        int dataIndex = 4;
        for(int i = 0; i < itemXs.length; i++){
            itemXs[i] = data[dataIndex];
            itemYs[i] = data[dataIndex + 1];

            dataIndex += 2;
        }
    }

    @Override
    public float[] getData() {
        float[] data = new float[4 + itemXs.length + itemYs.length];
        data[0] = booleanToFloat(connectNorth);
        data[1] = booleanToFloat(connectEast);
        data[2] = booleanToFloat(connectSouth);
        data[3] = booleanToFloat(connectWest);

        int dataIndex = 4;
        for(int i = 0; i < itemXs.length; i ++){
            data[dataIndex] = itemXs[i];
            data[dataIndex + 1] = itemYs[i];

            dataIndex += 2;
        }

        return data;
    }

    private boolean floatToBoolean(float data) {
        return data == 1;
    }

    private static float booleanToFloat(boolean connectNorth) {
        return connectNorth ? 1 : 0;
    }

    private float getMinXPos() {
        return connectWest ? 0 : 8;
    }

    private float getMinYPos() {
        return connectSouth ? 0 : 8;
    }

    private float getMaxXPos() {
        return connectEast ? Tile.SIZE : 24;
    }

    private float getMaxYPos(){
        return connectNorth ? Tile.SIZE : 24;
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

    public float[] getItemXs() {
        return itemXs;
    }

    public float[] getItemYs() {
        return itemYs;
    }
}
