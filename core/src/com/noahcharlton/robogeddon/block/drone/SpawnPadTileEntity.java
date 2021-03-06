package com.noahcharlton.robogeddon.block.drone;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.block.tileentity.TileEntitySelectable;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PoweredTileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.drone.DroneType;
import com.noahcharlton.robogeddon.entity.drone.RepairDroneEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.ArrayList;
import java.util.List;

public class SpawnPadTileEntity extends PoweredTileEntity implements TileEntitySelectable {

    public static final String subMenuID = "spawn_pad";

    private List<Entity> entities = new ArrayList<>();
    private int tick = 0;
    private boolean active = true;

    private DroneType currentType = (DroneType) EntityType.repairDroneEntity;

    public SpawnPadTileEntity(Tile rootTile, float usageRate) {
        super(rootTile, usageRate);
    }

    @Override
    public void update() {
        super.update();
        removeInvalidEntities();

        if(world.isServer() && entities.size() < 5 && active){
            usePower();

            if(hasPower()){
                tick++;
            }

            if(tick > currentType.getFormationTime()){
                 spawnEntity();
                 tick = 0;
                 dirty = true;
            }
        }

        if(world.isServer() && !active){
           tick++;

           if(tick > 140){
               tick = 0;

               checkShouldActivate();
           }
        }
    }

    private void checkShouldActivate() {
        for(int x = rootTile.getX() - 8; x <= rootTile.getX() + 8; x++){
            for(int y = rootTile.getY() - 8; y <= rootTile.getY() + 8; y++){
                var tile = world.getTileAt(x, y);

                if(tile != null && tile.getBlockHealth() < 1f){
                    active = true;
                    dirty = true;
                }
            }
        }
    }

    private void removeInvalidEntities() {
        for(var iterator = entities.iterator(); iterator.hasNext();){
            var entity = iterator.next();

            if(entity == null || entity.isDead()){
                iterator.remove();
                dirty = true;
            }
        }
    }

    @Override
    public float[] getData() {
        var entityIds = FloatUtils.toArray(entities, Entity::getId);
        var active = new float[]{this.active ? 1f : 0f};

        return FloatUtils.combineFloatArrays(active, entityIds);
    }

    @Override
    public void receiveData(float[] data) {
        entities.clear();

        active = data[0] == 1f;
        for(int i = 1; i < data.length; i++){
            entities.add(world.getEntityByID((int) data[i]));
        }

        dirty = true;
    }

    @Override
    public void save(XmlWriter xml) {
        super.save(xml);

        xml.element("EntityType", currentType.getTypeID());
    }

    @Override
    public void load(Element xml) {
        super.load(xml);

        if(xml.hasChild("EntityType")){
            currentType = (DroneType) Core.entities.get(xml.get("EntityType"));

            Server.runLater(() -> world.sendMessageToClient(new SpawnPadUpdateMessage(getRootTile(), currentType)));
        }
    }

    @Override
    public void onCustomMessageReceived(CustomTileEntityMessage message) {
        if(message instanceof SpawnPadUpdateMessage){
            currentType = ((SpawnPadUpdateMessage) message).getTypeAsDrone();

            if(world.isServer())
                world.sendMessageToClient(message);
        }else{
            super.onCustomMessageReceived(message);
        }
    }

    @Override
    protected void loadSaveData(float[] data) {
        //Do it after loading has taken place because entities might not have been loaded yet!
        Server.runLater(() -> {
            receiveData(data);
            dirty = true;
        });
    }

    private void spawnEntity() {
        Entity entity = currentType.create(world, Team.RED);
        entity.setX(rootTile.getPixelXCenter());
        entity.setY(rootTile.getPixelYCenter());

        ((ServerWorld) world).addEntity(entity);
        entities.add(entity);

        if(entity instanceof RepairDroneEntity)
            ((RepairDroneEntity) entity).setFocusedChunk(getRootTile().getChunk());
    }

    @Override
    public String[] getDetails() {
        if(active){
            return new String[]{
                    "Entity Count: " + entities.size(),
                    "Drone Type: " + currentType.getDisplayName(),
                    "Craft Time: " + FloatUtils.asIntString(currentType.getFormationTime() / 60f) + " seconds",
            };
        }else{
            return new String[]{
                    "Entity Count: " + entities.size(),
                    "Drone Type: " + currentType.getDisplayName(),
                    "Craft Time: " + FloatUtils.asIntString(currentType.getFormationTime() / 60f) + " seconds",
                    "\nDeactivated!"
            };
        }

    }

    @Override
    public String getSubMenuID() {
        return subMenuID;
    }

    public DroneType getCurrentType() {
        return currentType;
    }
}
