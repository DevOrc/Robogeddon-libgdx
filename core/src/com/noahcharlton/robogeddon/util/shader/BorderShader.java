package com.noahcharlton.robogeddon.util.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.noahcharlton.robogeddon.Core;

public class BorderShader{

    private static ShaderProgram program;
    private static Mesh mesh;

    public static void init(){
        Core.assets.registerShader("border").setOnLoad(p -> program = p);

        mesh = new Mesh(Shaders.getVertexDataType(), false, 4, 6,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "position"));

        Core.disposables.add(mesh);
    }

    public static void render(float x1, float y1, float width, float height, Color color){
        mesh.setVertices(new float[]{
                x1, y1,
                x1 + width, y1,
                x1, y1 + height,
                x1 + width, y1 + height,
        });
        mesh.setIndices(new short[]{
                2, 0, 1,
                3, 2, 1
        });

        program.setUniformf("team_color", color);
        Shaders.render(program, mesh);
    }

    public static ShaderProgram getProgram() {
        return program;
    }
}
