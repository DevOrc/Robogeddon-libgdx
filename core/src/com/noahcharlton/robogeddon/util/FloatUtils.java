package com.noahcharlton.robogeddon.util;

import java.util.List;

public class FloatUtils{

    public static final float PIf = (float) Math.PI;

    public static <T> float[] toArray(List<T> inputs, FloatFunction<T> mapper){
        float[] data = new float[inputs.size()];

        for(int i = 0; i < data.length; i++) {
            var input = inputs.get(i);

            if(input != null)
                data[i] = mapper.toFloat(inputs.get(i));
        }

        return data;
    }

    public static float[] combineFloatArrays(float[] array1, float[] array2){
        var result = new float[array1.length + array2.length];

        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);

        return result;
    }

    public static String asIntString(float productionRate) {
        return Integer.toString((int) productionRate);
    }

    public static String asString(float value, int frontDigits, int backDigits) {
        return String.format("%" + frontDigits + "."+ backDigits + "f", value);
    }
}
