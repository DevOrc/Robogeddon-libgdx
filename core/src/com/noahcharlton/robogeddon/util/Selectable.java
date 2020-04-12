package com.noahcharlton.robogeddon.util;

@Side(Side.CLIENT)
public interface Selectable {

    String getTitle();

    default String getDesc(){
        return "";
    }

    default String[] getDetails(){
        return new String[]{};
    }

    default boolean isInfoInvalid(){
        return false;
    }

    default void onInfoValidated(){

    }

}
