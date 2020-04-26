package com.noahcharlton.robogeddon.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.client.GameClient;
import com.noahcharlton.robogeddon.util.Selectable;
import com.noahcharlton.robogeddon.world.BuildBlockMessage;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;

import java.util.List;

public class BuildBlock implements BuildAction, Selectable {

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

            if(notEnoughResources() || !block.canBuildAt(tile, client.getWorld().getPlayersRobot())){
                renderRedCover(tile);
            }
        }
    }

    private void renderRedCover(Tile tile) {
        var shapeDrawer = client.getGameShapeDrawer();

        shapeDrawer.setColor(1f, 0f, 0f, .3f);
        shapeDrawer.filledRectangle(tile.getPixelX(), tile.getPixelY(), Tile.SIZE * block.getWidth(),
                Tile.SIZE * block.getHeight());
    }

    private boolean notEnoughResources() {
        for(ItemStack requirement: block.getRequirements()){
            if(client.getWorld().getInventoryForItem(requirement.getItem()) < requirement.getAmount()){
                return true;
            }
        }

        return false;
    }

    @Override
    public String getTitle() {
        return "Build " + block.getDisplayName();
    }

    @Override
    public String getDesc() {
        return "";
    }

    @Override
    public String[] getDetails() {
        List<ItemStack> requirements = block.getRequirements();

        if(requirements.size() == 0)
            return new String[]{"Requirements: None"};

        String[] info = new String[requirements.size() + 1];
        info[0] = "Requirements: ";

        for(int i = 0; i < requirements.size(); i++){
            info[i + 1] = requirements.get(i).getDisplayInfo();
        }

        return info;
    }

    @Override
    public boolean isInfoInvalid() {
        return false;
    }
}
