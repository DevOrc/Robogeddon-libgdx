package com.noahcharlton.robogeddon.block.tileentity;

public interface TileEntitySelectable {

    default String getDesc(){
        return "";
    };

    default String[] getDetails(){
        return new String[]{};
    }

    default String getSubMenuID(){
        return null;
    }

}
