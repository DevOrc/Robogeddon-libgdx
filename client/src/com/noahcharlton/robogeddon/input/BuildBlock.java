package com.noahcharlton.robogeddon.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.client.GameClient;
import com.noahcharlton.robogeddon.world.BuildBlockMessage;
import com.noahcharlton.robogeddon.world.Tile;

public class BuildBlock implements BuildAction {

    protected final GameClient client = GameClient.getInstance();
    protected Block block;

    public BuildBlock(Block block) {
        this.block = block;
    }

    @Override
    public String getName() {
        return "BuildBlock(" + block.getTypeID() +")";
    }

    @Override
    public void onClick(Tile tile, int button) {
        Log.debug("Building block " + block.getTypeID() + " on " + tile);
        var player = client.getWorld().getPlayersRobot().getId();

        if(button == Input.Buttons.RIGHT){
            if(tile.hasBlock()){
                BuildBlockMessage message = new BuildBlockMessage(tile, null, player);
                client.getWorld().sendMessageToServer(message);
            }else{
                client.getProcessor().setBuildAction(null);
            }
        }else{
            BuildBlockMessage message = new BuildBlockMessage(tile, block, player);
            client.getWorld().sendMessageToServer(message);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector3 pos = Core.client.mouseToWorld();
        Tile tile = client.getWorld().tileFromPixel(pos);

        if(tile != null) {
            block.getRenderer().buildRender(batch, tile);
        }
    }
}
