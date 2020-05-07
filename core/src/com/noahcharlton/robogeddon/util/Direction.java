package com.noahcharlton.robogeddon.util;

public enum Direction {

    NORTH, SOUTH, EAST, WEST;

    public int getDeltaX() {
        switch(this) {
            case NORTH:
            case SOUTH:
                return 0;
            case EAST:
                return 1;
            case WEST:
                return -1;
        }

        throw new RuntimeException();
    }

    public int getDeltaY() {
        switch(this) {
            case NORTH:
                return 1;
            case SOUTH:
                return -1;
            case EAST:
            case WEST:
                return 0;
        }

        throw new RuntimeException();
    }

    public Direction flip() {
        switch(this) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
        }

        throw new RuntimeException();
    }

    public boolean isNorthSouth(){
        return this == NORTH || this == SOUTH;
    }

    public boolean isEastWest(){
        return this == EAST || this == WEST;
    }

    @Override
    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }
}
