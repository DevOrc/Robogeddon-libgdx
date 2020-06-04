package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.drone.AttackDroneEntity;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.message.ClearChatMessage;
import com.noahcharlton.robogeddon.message.TutorialCompleteMessage;
import com.noahcharlton.robogeddon.util.Side;

@Side(Side.SERVER)
public class TutorialHandler {

    enum TutorialStage {
        PRE_MOVEMENT, AFTER_MOVEMENT, SHOOT_INFO, SPAWN_ENEMY, VICTORY;

        int getStageInt(){
            for(int i = 0; i < values().length; i++){
                if(values()[i] == this){
                    return i;
                }
            }

            return -1;
        }

        static TutorialStage fromInt(int value){
            return TutorialStage.values()[value];
        }
    }

    private final ServerWorld world;

    private int stage;
    private long nextStageSwitch;

    public TutorialHandler(ServerWorld world) {
        this.world = world;

        stage = TutorialStage.PRE_MOVEMENT.getStageInt();
        nextStageSwitch = Long.MAX_VALUE;
    }

    public void update(){
        if(nextStageSwitch < System.currentTimeMillis()){
            gotoNextStage();
        }

        switch(TutorialStage.fromInt(stage)){
            case PRE_MOVEMENT:
                if(getPlayer().getVelocity() > .2f){
                    gotoNextStage();
                }
                break;
            case SPAWN_ENEMY:
                if(!isDroneAlive()){
                    gotoNextStage();
                }
                break;
        }
    }

    private boolean isDroneAlive() {
        return world.getEntities().stream().anyMatch(entity -> entity instanceof AttackDroneEntity);
    }

    private void onStageSwitch(TutorialStage newStage){
        switch(newStage){
            case AFTER_MOVEMENT:
                nextStageSwitch = System.currentTimeMillis() + 3000;
                break;
            case SHOOT_INFO:
                clearChat();
                sendChat("You can shoot by pressing space bar!");
                nextStageSwitch = System.currentTimeMillis() + 8000;
                break;
            case SPAWN_ENEMY:
                var entity = EntityType.attackDroneEntity.create(world, world.getEnemyTeam());
                entity.setY(-Chunk.SIZE * Tile.SIZE);
                ((AttackDroneEntity) entity).useTarget(getPlayer());

                world.addEntity(entity);
                break;
            case VICTORY:
                world.sendMessageToClient(new TutorialCompleteMessage());
                break;
        }
    }

    private Entity getPlayer(){
        return world.getPlayers().values().iterator().next();
    }

    private void gotoNextStage(){
        stage += 1;
        nextStageSwitch = Long.MAX_VALUE;

        onStageSwitch(TutorialStage.fromInt(stage));
    }

    private void sendChat(String message){
        world.sendMessageToClient(new ChatMessage(message));
    }

    private void clearChat(){
        world.sendMessageToClient(new ClearChatMessage());
    }
}
