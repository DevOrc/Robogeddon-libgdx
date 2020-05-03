package com.noahcharlton.robogeddon.block.tileentity.electricity;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.util.Selectable;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;
import com.noahcharlton.robogeddon.world.electricity.ServerPowerGraph;

public interface HasElectricity {

    default void updateElectricity(){
        TileEntity entity = getTileEntity(this);

        if(!isConnectedToRelay()){
            findRelay(entity, this);
        }
    }

    private static void findRelay(TileEntity entity, HasElectricity electricity){
        Tile thisTile = entity.getRootTile();
        var graph = electricity.getTeamPowerGraph();

        int chunkRadius = Chunk.SIZE / 2;

        for(int x = thisTile.getX() - chunkRadius; x <= thisTile.getX() + chunkRadius; x++){
            for(int y = thisTile.getY() - chunkRadius; y <= thisTile.getY() + chunkRadius; y++){
                Tile relayTile = thisTile.getWorld().getTileAt(x, y);

                if(relayTile != null && relayTile.getBlock() == Blocks.relayBlock){
                    electricity.setRelayTile(relayTile);

                    if(graph instanceof ServerPowerGraph){
                        ((ServerPowerGraph) graph).add(thisTile, electricity);
                    }
                    return;
                }
            }
        }

        //Make sure the old one is removed if we don't get a new one
        electricity.setRelayTile(null);
    }

    private static TileEntity getTileEntity(HasElectricity hasElectricity){
        if(!(hasElectricity instanceof TileEntity)){
            throw new IllegalStateException("Only tile entities can have electricity: " + hasElectricity.getClass());
        }

        return (TileEntity) hasElectricity;
    }

    default boolean isConnectedToRelay(){
        var tile = getRelayTile();

        return tile != null && tile.getBlock() == Blocks.relayBlock;
    }

    default String[] getElectricityDetails() {
        var power = getTeamPowerGraph();

        if(power == null || !isConnectedToRelay())
            return new String[]{"Not connected to team power!!"};

        var details = new String[]{
                "Team Power Generated: " + String.format("%2.1f", power.getGeneratedPower()),
                "Team Power Consumed: " + String.format("%2.1f", power.getConsumedPower()),
                String.format("Team Power Used: %2.0f%%", getPowerUsed(power) * 100)
        };

        return Selectable.combineArrays(details, getPowerBuffer().getPowerBufferDetails());
    }

    private static float getPowerUsed(PowerGraph power) {
        if(power.getGeneratedPower() == 0)
            return 1f;
        return power.getConsumedPower() / power.getGeneratedPower();
    }

    void setRelayTile(Tile tile);

    Tile getRelayTile();

    PowerBuffer getPowerBuffer();

    PowerGraph getTeamPowerGraph();
}
