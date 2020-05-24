package com.noahcharlton.robogeddon.block.drone;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.drone.DroneType;
import com.noahcharlton.robogeddon.world.Tile;

public class SpawnPadUpdateMessage extends CustomTileEntityMessage {

    private final String droneType;

    public SpawnPadUpdateMessage(Tile tile, EntityType type) {
        super(tile);

        this.droneType = type.getTypeID();
    }

    public String getDroneType() {
        return droneType;
    }

    public DroneType getTypeAsDrone(){
        return (DroneType) Core.entities.get(droneType);
    }
}
