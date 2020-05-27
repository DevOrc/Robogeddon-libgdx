package com.noahcharlton.robogeddon.util;

import com.noahcharlton.robogeddon.util.help.HelpInfo;

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

    default String getSubMenuID(){
        return null;
    }

    default HelpInfo getHelpInfo(){
        return null;
    }

    static String[] combineArrays(String[] array1, String[] array2){
        var result = new String[array1.length + array2.length];

        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);

        return result;
    }

}
