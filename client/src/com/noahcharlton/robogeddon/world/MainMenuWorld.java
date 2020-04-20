package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.NewEntityMessage;
import com.noahcharlton.robogeddon.entity.RobotEntity;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.settings.NewWorldSettings;

public class MainMenuWorld extends ClientWorld {

    public MainMenuWorld() {
        super(new NewWorldSettings());
    }

    @Override
    public void update() {
        super.update();

        if(getPlayersRobot() != null){
            ((RobotEntity) getPlayersRobot()).setControlling(false);
            getPlayersRobot().setVelocity(2f);

            if(getPlayersRobot().getY() > Chunk.SIZE * Tile.SIZE * 2){
                getPlayersRobot().setY(Chunk.SIZE * Tile.SIZE * -1);
            }
        }
    }

    @Override
    protected boolean onMessageReceived(Message message) {
        if(message instanceof AssignRobotMessage){
            super.onMessageReceived(message);
        }if(message instanceof WorldSyncMessage){
            super.onMessageReceived(message);
        }else if(message instanceof NewEntityMessage){
            var entity = ((NewEntityMessage) message).getEntityType();

            if(entity.equals(EntityType.robotEntity.getTypeID())){
                super.onMessageReceived(message);
            }
        }

        return false;
    }

    @Override
    public void render(SpriteBatch batch) {
        chunks.values().forEach(chunk -> chunk.renderFloors(batch));
        chunks.values().forEach(chunk -> chunk.renderBlocks(batch, 0));

        if(getPlayersRobot() != null)
            getPlayersRobot().getType().render(batch, getPlayersRobot());
    }

    @Override
    public String toString() {
        return "Main Menu World";
    }
}
