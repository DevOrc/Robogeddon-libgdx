package com.noahcharlton.robogeddon.command;

import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.world.ServerWorld;

import java.util.List;
import java.util.function.BiConsumer;

public class Command implements HasID {

    private final String name;
    private final BiConsumer<ServerWorld, List<Argument>> arguments;

    public Command(String name, BiConsumer<ServerWorld, List<Argument>>  arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public void execute(ServerWorld world, List<Argument> args){
        arguments.accept(world, args);
    }

    @Override
    public String getTypeID() {
        return name;
    }
}
