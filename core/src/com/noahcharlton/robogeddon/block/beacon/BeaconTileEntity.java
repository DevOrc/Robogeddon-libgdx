package com.noahcharlton.robogeddon.block.beacon;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntitySelectable;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.team.Team;

public class BeaconTileEntity extends TileEntity implements TileEntitySelectable {

    static final int CLAIM_TIME = 1800;
    static final int NEUTRALIZE_TIME = 300;

    private final Team team;
    private int time;

    public BeaconTileEntity(Tile rootTile, Team team) {
        super(rootTile);

        this.team = team;
    }

    @Override
    public void update() {
        if(world.isClient()){
            time++;
            if(time >= 200)
                time = 0;
        }else{
            time++;

            var chunkTeam = getRootTile().getChunk().getTeam();

            if(time >= CLAIM_TIME && chunkTeam == Team.NEUTRAL){
                getRootTile().getChunk().setTeam(team);
                selfDestruct();
            }else if(time >= NEUTRALIZE_TIME && chunkTeam != Team.NEUTRAL && team != chunkTeam){
                getRootTile().getChunk().setTeam(Team.NEUTRAL);
                selfDestruct();
            }
        }
    }

    private void selfDestruct() {
        var world = (ServerWorld) this.world;
        world.destroyBlock(getRootTile());
    }

    @Side(Side.CLIENT)
    public int getRenderSide(){
        return Math.min(time / 50, 3);
    }

    @Override
    public String getDesc() {
        return "Team: " + team.getDisplayName();
    }

    @Override
    protected float[] getSaveData() {
        return new float[]{time};
    }

    @Override
    protected void loadSaveData(float[] data) {
        time = (int) data[0];
    }
}
