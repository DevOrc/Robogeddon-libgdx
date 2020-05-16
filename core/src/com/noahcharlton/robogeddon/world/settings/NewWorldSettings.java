package com.noahcharlton.robogeddon.world.settings;

import com.noahcharlton.robogeddon.world.gen.WorldGenerator;

import java.util.Random;

public class NewWorldSettings implements WorldSettings {

    private final long seed;

    public NewWorldSettings(long seed) {
        this.seed = seed;
    }

    public NewWorldSettings() {
        this(new Random().nextLong());
    }

    public WorldGenerator createGenerator(){
        return new WorldGenerator(seed);
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "NewWorldSettings(seed="  + seed + ")";
    }
}
