package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.drone.AttackDroneEntity;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.message.ClearChatMessage;
import com.noahcharlton.robogeddon.message.TutorialCompleteMessage;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.item.Items;

@Side(Side.SERVER)
public class TutorialHandler {

    enum TutorialStage {
        PRE_MOVEMENT, AFTER_MOVEMENT, SHOOT_INFO, SPAWN_ENEMY, MANUAL_MINE, BUILD_MINER, BUILD_ITEM_DUCT, VICTORY;

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
            case MANUAL_MINE:
                if(world.getInventoryForItem(Items.iron) >= 15){
                    gotoNextStage();
                }
                break;
            case BUILD_MINER:
                for(int x = -Chunk.SIZE; x < Chunk.SIZE; x++){
                    for(int y = -Chunk.SIZE; y < Chunk.SIZE; y++){
                        if(world.getTileAt(x, y).getBlock() == Blocks.minerBlock){
                            gotoNextStage();
                        }
                    }
                }
                break;
            case BUILD_ITEM_DUCT:
                if(world.getInventoryForItem(Items.iron) >= 50){
                    gotoNextStage();
                }
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
            case MANUAL_MINE:
                clearChat();
                sendChat("Lets start gathering some resources!");
                sendChat("You can use the mining laser by right clicking");
                sendChat("");
                sendChat("Gather 15[ORANGE] iron ore[WHITE] so we can build a miner");
                break;
            case BUILD_MINER:
                clearChat();
                sendChat("Now lets build a miner: \n");
                sendChat("To build blocks, use the menu in the lower left hand corner.");
                sendChat("");
                sendChat("I would build it on some [ORANGE] iron ore[WHITE], because");
                sendChat("you will need a lot of it.");
                world.unlockBlock(Blocks.minerBlock, false);
                world.buildBlock(world.getTileAt(-1, -1), Blocks.inventoryPortal);
                break;
            case BUILD_ITEM_DUCT:
                clearChat();
                sendChat("Now lets build an item duct from the miner to");
                sendChat("the inventory portal. \n\nYou might need to gather some");
                sendChat("more [ORANGE] iron ore[WHITE] and[GRAY] rocks[WHITE]");
                sendChat("\nYou can use Q to rotate the direction of the item duct.");
                sendChat("\n\nGather 50 iron to complete the tutorial!");
                world.unlockBlock(Blocks.itemDuctNorth, false);
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
