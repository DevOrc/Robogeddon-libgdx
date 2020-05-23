package com.noahcharlton.robogeddon.entity.drone;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public class RepairDroneEntity extends AbstractDroneEntity {

    private static float SEARCH_RADIUS = 900;

    private Chunk focusedChunk;
    private Tile healingTile;

    private int tick;
    private float movementTime;

    public RepairDroneEntity(EntityType type, World world, Team team) {
        super(type, world, team);

        angle = (float) (Math.PI / 2f);
        focusedChunk = world.getChunkAt(0, 0);
    }

    @Override
    protected void update() {
        if(world.isServer() && isInWorld())
            updateHealingTile();
    }

    @Override
    public void updateKinematics() {
        velocity = 0f;

        if(healingTile == null){
            if(isOnCircle()){
                movementTime += .005f;

                x = (float) (Math.cos(movementTime) * SEARCH_RADIUS);
                y = (float) (Math.sin(movementTime) * SEARCH_RADIUS);
                angle = (float) ((movementTime % (Math.PI * 2)) + (Math.PI / 2f));
            }else{
                angle = new Vector2((float) (Math.cos(movementTime) * SEARCH_RADIUS),
                        (float) (Math.sin(movementTime) * SEARCH_RADIUS)).sub(x, y).angleRad();
                x += Math.cos(angle) * 3.25f;
                y += Math.sin(angle) * 3.25f;
            }
        }else{
            angle = new Vector2(healingTile.getPixelXCenter(), healingTile.getPixelYCenter()).sub(x, y).angleRad();
        }

        setDirty(true);
    }

    @Override
    public boolean isEngineOn() {
        return healingTile == null;
    }

    private boolean isOnCircle() {
        var xDiff = x - (float) (Math.cos(movementTime) * SEARCH_RADIUS);
        var yDiff = y - (float) (Math.sin(movementTime) * SEARCH_RADIUS);
        return Math.abs(xDiff) < 10 && Math.abs(yDiff) < 10;
    }

    private void updateHealingTile(){
        int waitLength = 30;

        if(healingTile == null){
            if(tick <= 0){
                searchForHealingTile();
                tick = waitLength;
            }else{
                tick--;
            }
        }else if(healingTile.getBlockHealth() < 1f){
            tick--;

            if(tick <= 0){
                tick = waitLength;
                healingTile.healDamage();
            }
        }else{
            setHealingTile(null);
        }
    }

    private void setHealingTile(Tile tile) {
        this.healingTile = tile;

        world.sendMessageToClient(new RepairTargetMessage(getId(), tile));
    }

    private void searchForHealingTile() {
        if(!searchChunk(focusedChunk)){
            searchChunk(getTile().getChunk());
        }
    }

    private boolean searchChunk(Chunk chunk) {
        for(int x = 0; x < Chunk.SIZE; x++){
            for(int y = 0; y < Chunk.SIZE; y++){
                var tile = chunk.getTile(x, y);

                if(tile.getBlockHealth() < 1){
                    setHealingTile(tile);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void customRender(SpriteBatch batch) {
        if(healingTile == null)
            return;

        var sr = Core.client.getGameShapeDrawer();
        sr.setColor(new Color(1f, 1f, 1f, .25f));

        sr.line(x, y, healingTile.getPixelXCenter(), healingTile.getPixelYCenter(), 4);
        sr.filledCircle(healingTile.getPixelXCenter(), healingTile.getPixelYCenter(), 2);
    }

    public void setFocusedChunk(Chunk focusedChunk) {
        this.focusedChunk = focusedChunk;

        world.sendMessageToClient(new RepairChunkMessage(getId(), focusedChunk));
    }

    @Override
    public void onCustomMessageReceived(CustomEntityMessage message) {
        if(message instanceof RepairTargetMessage){
            healingTile = ((RepairTargetMessage) message).getTile(world);
            System.out.println("Tile set: " + healingTile);
        }else if(message instanceof RepairChunkMessage){
            focusedChunk = ((RepairChunkMessage) message).getChunk(world);
        }
    }

    static class RepairTargetMessage extends CustomEntityMessage {

        private final int tileX;
        private final int tileY;

        public RepairTargetMessage(int ID, Tile tile) {
            super(ID);

            if(tile == null){
                tileX = Integer.MIN_VALUE;
                tileY = Integer.MIN_VALUE;
            }else{
                this.tileX = tile.getX();
                this.tileY = tile.getY();
            }
        }

        @Side(Side.CLIENT)
        Tile getTile(World world){
            return world.getTileAt(tileX, tileY);
        }
    }

    static class RepairChunkMessage extends CustomEntityMessage {

        private final int chunkX;
        private final int chunkY;

        public RepairChunkMessage(int ID, Chunk chunk) {
            super(ID);
            this.chunkX = chunk.getLocation().x;
            this.chunkY = chunk.getLocation().y;
        }

        @Side(Side.CLIENT)
        Chunk getChunk(World world){
            return world.getChunkAt(chunkX, chunkY);
        }
    }
}
