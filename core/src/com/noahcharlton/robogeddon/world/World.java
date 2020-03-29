package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.util.Side;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Side(Side.BOTH)
public abstract class World {

    /** These variables are set on creation for the server world, but for the client they are sent
     * when the server sends the first worldSync message*/
    private int height = -1;
    private int width = -1;
    private Tile[][] tiles;

    private final boolean isServer;
    protected final List<Entity> entities = new LinkedList<>();

    World(boolean isServer) {
        this.isServer = isServer;
    }

    public Tile getTileAt(int x, int y){
        if(x < 0 || y < 0 || x >= width || y>=height){
            return null;
        }

        return tiles[x][y];
    }

    protected Entity getEntityByID(int id){
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
        }else{
            return false;
        }

        return true;
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
        entities.forEach(Entity::onUpdate);

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

    public Tile tileFromPixel(Vector3 pos){
        if(pos == null)
            return null;

        return tileFromPixel(new Vector2(pos.x, pos.y));
    }

    public Tile tileFromPixel(Vector2 pos){
        if(pos == null)
            return null;

        return getTileAt((int) pos.x / Tile.SIZE, (int)pos.y / Tile.SIZE);
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isClient(){
        return !isServer;
    }

    void setHeight(int height) {
        if(this.height != -1)
            throw new UnsupportedOperationException("Height has already been set.");
        this.height = height;
    }

    void setWidth(int width) {
        if(this.width != -1)
            throw new UnsupportedOperationException("Width has already been set.");
        this.width = width;
    }

    void setTiles(Tile[][] tiles) {
        if(this.tiles != null)
            throw new UnsupportedOperationException("Tiles have already been created.");
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixelWidth() {
        return width * Tile.SIZE;
    }

    public int getPixelHeight() {
        return height * Tile.SIZE;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
