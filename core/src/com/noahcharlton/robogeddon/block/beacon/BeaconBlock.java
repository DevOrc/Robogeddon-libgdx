package com.noahcharlton.robogeddon.block.beacon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.team.Team;

public class BeaconBlock extends Block implements BlockRenderer, HasTileEntity {

    private final Team team;

    private static boolean texturesRegistered = false;
    private static TextureRegion baseTexture;
    private static TextureRegion topTexture;

    public BeaconBlock(String idBase, Team team) {
        super(idBase + "_" + team.name());
        this.team = team;
    }

    @Override
    public String getDisplayName() {
        return "Team Beacon";
    }

    @Override
    public void initRenderer() {
        renderer = this;
        if(texturesRegistered)
            return;

        texturesRegistered = true;
        Core.assets.registerTexture("blocks/beacon_base").setOnLoad(t -> baseTexture = t);
        Core.assets.registerTexture("blocks/beacon_top").setOnLoad(t -> topTexture = t);
    }

    @Override
    public boolean canBuildAt(Tile tile, Entity placer) {
        return !isBeaconInChunk(tile.getChunk()) && team != tile.getChunk().getTeam();
    }

    private boolean isBeaconInChunk(Chunk chunk) {
        for(int x = 0; x < Chunk.SIZE; x++){
            for(int y = 0; y < Chunk.SIZE; y++){
                var tile = chunk.getTile(x, y);

                if(tile.getBlock() instanceof BeaconBlock){
                    if(((BeaconBlock) tile.getBlock()).team == team)
                        return true;
                }
            }
        }

        return false;
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.draw(baseTexture, x, y);
        drawLight(tile);
        batch.draw(topTexture, x, y);
    }

    private void drawLight(Tile tile) {
        int side = ((BeaconTileEntity) tile.getTileEntity()).getRenderSide();
        var drawer = Core.client.getGameShapeDrawer();

        float x = tile.getPixelX();
        float y = tile.getPixelY();
        float width = 0;
        float height = 0;

        switch(side){
            case 0:
                x += 5;
                y += 2;
                width = 22;
                height = 2;
                break;
            case 1:
                x += 2;
                y += 7;
                width = 2;
                height = 21;
                break;
            case 2:
                x += 5;
                y += 28;
                width = 22;
                height = 2f;
                break;
            case 3:
                x += 28;
                y += 6;
                width = 2;
                height = 21;
                break;
            default:
                Log.warn("Unknown side number for beacon: " + side);
        }

        var color = team.getColor();
        color.a = .5f;
        drawer.setColor(color);
        drawer.filledRectangle(x, y, width, height);
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.setColor(1f, 1f, 1f, .5f);
        batch.draw(baseTexture, x, y);
        batch.draw(topTexture, x, y);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new BeaconTileEntity(tile, team);
    }
}
