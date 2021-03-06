package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;
import com.noahcharlton.robogeddon.world.item.Inventory;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Side(Side.BOTH)
public abstract class World {

    protected final ObjectMap<GridPoint2, Chunk> chunks = new ObjectMap<>();
    private final boolean isServer;

    protected final Inventory inventory = new Inventory();
    protected final List<Entity> entities = new LinkedList<>();
    protected boolean paused;
    protected final Array<Block> unlockedBlocks = new Array<>(Core.blocks.size());

    /**
     * Used to reduce allocations in getTileAt / getChunkAt
     */
    private final GridPoint2 tempGridpoint = new GridPoint2();

    World(boolean isServer) {
        this.isServer = isServer;
    }

    public Tile getTileAt(int x, int y){
        var chunk = getChunkFromTile(x, y);

        if(chunk == null)
            return null;

        //Add one on negative side because it starts at -1
        //And the array starts at 0
        if(x < 0)
            x++;

        if(y < 0)
            y++;

        var chunkX = Math.abs(x % 32);
        var chunkY = Math.abs(y % 32);

        return chunk.getTile(chunkX, chunkY);
    }

    public Chunk getChunkAt(int chunkX, int chunkY){
        tempGridpoint.x = chunkX;
        tempGridpoint.y = chunkY;

        return chunks.get(tempGridpoint);
    }

    public Chunk getChunkFromTile(int tileX, int tileY){
        tempGridpoint.x = Math.floorDiv(tileX, Chunk.SIZE);
        tempGridpoint.y = Math.floorDiv(tileY, Chunk.SIZE);

        return chunks.get(tempGridpoint);
    }

    public Entity getEntityByID(int id){
        for(Entity entity: entities){
            if(entity.getId() == id){
                return entity;
            }
        }

        return null;
    }

    protected boolean onMessageReceived(Message message){
        if(message instanceof CustomEntityMessage){
            onCustomEntityMessage((CustomEntityMessage) message);
        }else if(message instanceof CustomTileEntityMessage){
            onCustomTileEntityMessage((CustomTileEntityMessage) message);
        }else{
            return false;
        }

        return true;
    }

    private void onCustomTileEntityMessage(CustomTileEntityMessage message) {
        var tile = getTileAt(message.getX(), message.getY());

        if(tile != null && tile.getTileEntity() != null){
            tile.getTileEntity().onCustomMessageReceived(message);
        }else{
            Log.error("Received invalid custom tile entity message: " + message);
        }
    }

    private void onCustomEntityMessage(CustomEntityMessage message) {
        var entity = getEntityByID(message.getID());

        if(entity == null){
            Log.warn("Non-existent entity " + message.getID() + " sent message of type "
                    + message.getClass().getName());
        }else{
            getEntityByID(message.getID()).onCustomMessageReceived(message);
        }
    }

    public void update(){
        if(paused)
            throw new IllegalStateException();

        chunks.values().forEach(Chunk::update);
        entities.forEach(Entity::onUpdate);

        if(isClient())
            return;

        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();

            if(entity.isDead()){
                it.remove();
                onEntityDead(entity);
            }
        }
    }

    protected void onEntityDead(Entity entity) {
        throw new UnsupportedOperationException("Only the server can have dead entities");
    }

    /**
     * If on the server, it sends the message to the client. If its on the
     * client, it sends the message to the server.
     */
    private void sendMessage(Message message){
        if(isServer){
            sendMessageToClient(message);
        }else{
            sendMessageToServer(message);
        }
    }

    public void sendMessageToClient(Message m){
        throw new UnsupportedOperationException();
    }

    public void sendMessageToServer(Message m){
        throw new UnsupportedOperationException();
    }

    public abstract PowerGraph getPowerForTeam(Team team);

    public Tile tileFromPixel(Vector3 pos){
        if(pos == null)
            return null;

        return tileFromPixel(new Vector2(pos.x, pos.y));
    }

    public Tile tileFromPixel(Vector2 pos){
        if(pos == null)
            return null;

        return tileFromPixel(pos.x, pos.y);
    }

    public Tile tileFromPixel(float x, float y){
        return getTileAt(Math.floorDiv((int) x, Tile.SIZE),  Math.floorDiv((int) y, Tile.SIZE));
    }

    public int getInventoryForItem(Item item){
        return inventory.getItem(item);
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isClient(){
        return !isServer;
    }

    public boolean isPaused() {
        return paused;
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public Array<Block> getUnlockedBlocks() {
        return unlockedBlocks;
    }
}
