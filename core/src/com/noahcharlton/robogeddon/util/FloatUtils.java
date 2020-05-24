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
}
