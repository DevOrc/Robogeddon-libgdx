package com.noahcharlton.robogeddon.util.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.noahcharlton.robogeddon.Core;

public class Shaders {

    public static void init(){
        BorderShader.init();
    }

    public static void render(ShaderProgram program, Mesh mesh){
        render(program, mesh, Core.client.getGameCamera());
    }

    public static void render(ShaderProgram program, Mesh mesh, Camera camera) {
        camera.update();

        program.setUniformMatrix("cam_combined", camera.combined);
        mesh.bind(program);
        mesh.render(program, GL20.GL_TRIANGLES);
    }

    public static Mesh.VertexDataType getVertexDataType() {
        return (Gdx.gl30 != null) ?
                Mesh.VertexDataType.VertexBufferObjectWithVAO : Mesh.VertexDataType.VertexArray;
    }
}
