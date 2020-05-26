package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderAsset extends Asset<ShaderProgram> {

    private final String name;
    private ShaderProgram shader;

    public ShaderAsset(String name) {
        this.name = name;
    }

    @Override
    protected String getName() {
        return "ShaderProgram(" + name + ")";
    }

    @Override
    protected ShaderProgram load() {
        var path = "shaders/" + name;

        shader = new ShaderProgram(Gdx.files.internal(path + ".vert"), Gdx.files.internal(path + ".frag"));
        if(!shader.isCompiled()){
            throw new GdxRuntimeException("Failed to compile shader " + path + ": " + shader.getLog());
        }

        return shader;
    }

    @Override
    protected void dispose() {
        shader.dispose();
    }
}
