package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.Floors;

public class Biomes {

    private static final Biome generic = new GenericBiome("General", 0, 0);
    private static final Biome desert = new DesertBiome("Desert", -1, -.4);

    public static void init() {
        Core.biomes.register(generic);
        Core.biomes.register(desert);
    }

    public static Biome get(double value) {
        for(Biome biome : Core.biomes.values()) {
            if(biome.isInRange(value)) {
                return biome;
            }
        }

        return generic;
    }

    static class GenericBiome extends Biome {

        GenericBiome(String name, double lowerVal, double higherVal) {
            super(name, lowerVal, higherVal);
        }

        @Override
        public boolean isInRange(double val) {
            return false;
        }

        @Override
        public void gen(Tile tile, double noise) {
            if(noise > .5) {
                tile.setFloor(Floors.rock, false);

                if(noise > .7) {
                    tile.setUpperFloor(Floors.ironOre, false);
                }

                return;
            }

            if(noise < -.5) {
                tile.setFloor(Floors.rock, false);

                if(noise < -.7) {
                    tile.setUpperFloor(Floors.coalOre, false);
                }

                return;
            }

            var dirtVal = (Math.abs(noise) % .1) * 10;

            if(dirtVal < .2) {
                tile.setFloor(Floors.rockyDirt, false);
            } else if(dirtVal < .6) {
                tile.setFloor(Floors.dirt2, false);
            } else {
                tile.setFloor(Floors.dirt1, false);
            }
        }
    }

    static class DesertBiome extends Biome {

        DesertBiome(String name, double lowerVal, double higherVal) {
            super(name, lowerVal, higherVal);
        }

        @Override
        public void gen(Tile tile, double noise) {
            if(noise > .5) {
                tile.setFloor(Floors.rockySand, false);

                if(noise > .7) {
                    tile.setFloor(Floors.sand, false);
                    tile.setUpperFloor(Floors.ironOre, false);
                }

                return;
            }

            if(noise < -.5) {
                tile.setFloor(Floors.rockySand, false);

                if(noise < -.7) {
                    tile.setFloor(Floors.sand, false);
                    tile.setUpperFloor(Floors.coalOre, false);
                }

                return;
            }

            var dirtVal = (Math.abs(noise) % .1) * 10;

            if(dirtVal < .6) {
                tile.setFloor(Floors.sand, false);
            } else {
                tile.setFloor(Floors.sand2, false);
            }
        }
    }
}
