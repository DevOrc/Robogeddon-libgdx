package com.noahcharlton.robogeddon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;

public class ShaderUtils {

    public static Mesh.VertexDataType getVertexDataType() {
        return (Gdx.gl30 != null) ?
                Mesh.VertexDataType.VertexBufferObjectWithVAO : Mesh.VertexDataType.VertexArray;
    }
}
