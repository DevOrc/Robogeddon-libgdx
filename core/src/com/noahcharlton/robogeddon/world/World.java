package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public abstract class World {

    protected final List<Entity> entities = new LinkedList<>();

    World() {

    }
}
