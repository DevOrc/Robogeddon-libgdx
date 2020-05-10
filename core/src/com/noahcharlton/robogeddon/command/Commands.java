package com.noahcharlton.robogeddon.command;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Commands {

    public static void preInit(){
        register("give_items", Commands::giveItem);
    }

    private static void giveItem(ServerWorld world, List<Argument> arguments) {
        for(Item item : Core.items.values()){
            world.getInventory().setItem(item, 1000);
        }

        System.out.println(arguments);
    }

    private static void register(String name, BiConsumer<ServerWorld, List<Argument>> handler){
        Core.commands.register(new Command(name, handler));
    }

    public static void runCommand(ServerWorld world, String name, String args) {
        Log.info("Running command(" + name + "): " + args);
        var command = Core.commands.getOrNull(name);

        if(command == null)
            throw new CommandParseException("No command with name: " + name);

        command.execute(world, parseArgs(args));
    }

    private static List<Argument> parseArgs(String args) {
        String[] argArray = args.split(" ");

        return Arrays.stream(argArray).filter(s -> !s.isBlank()).map(Argument::new).collect(Collectors.toList());
    }

    public static void parse(ServerWorld world, String message){
        if(!message.startsWith("/")){
            throw new CommandParseException("Commands must start with a slash!");
        }else if(message.startsWith("/ ")){
            throw new CommandParseException("Commands cannot have a space after the slash!");
        }

        Log.info("Parsing: " + message);
        int nameIndex = message.indexOf(" ");
        String args;
        String name;

        if(nameIndex == -1){
            name = message.substring(1);
            args = "";
        }else{
            name = message.substring(1, nameIndex);
            args = message.substring(nameIndex);
        }

        runCommand(world, name, args);
    }

    public static void handle(ServerWorld world, String text) {
        try{
            parse(world, text);
        }catch(CommandParseException e){
            world.sendMessageToClient(new ChatMessage("[RED][[PARSE ERROR][WHITE]  " + e.getMessage()));
        }catch(RuntimeException e){
            world.sendMessageToClient(new ChatMessage("[RED][[INTERNAL ERROR][WHITE]  " + e.getMessage()));
        }
    }
}
