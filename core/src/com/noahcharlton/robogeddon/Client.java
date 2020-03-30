package com.noahcharlton.robogeddon;

import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.util.Side;
import space.earlygrey.shapedrawer.ShapeDrawer;

@Side(Side.CLIENT)
public interface Client {

    /**
     * @return the world coordinates of the mouse
     */
    Vector3 mouseToWorld();

    ShapeDrawer getGameShapeDrawer();

    boolean isMouseOnUI();
}
