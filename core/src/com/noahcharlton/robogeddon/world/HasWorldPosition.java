package com.noahcharlton.robogeddon.world;

public interface HasWorldPosition {

    float getWorldXPos();

    float getWorldYPos();

    /**
     * @return if the entity/tile is still valid:
     * <br>
     * For tiles, the tile needs to have a block.
     * <br>
     * For entities, the entity must be alive
     */
    boolean isWorldPositionValid();
}
