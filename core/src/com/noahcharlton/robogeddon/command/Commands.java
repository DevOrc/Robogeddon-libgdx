package com.noahcharlton.robogeddon.command;

import com.badlogic.gdx.files.FileHandle;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.RobotEntity;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.util.GameData;
import com.noahcharlton.robogeddon.util.IntermediateDirection;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.floor.Floors;
import com.noahcharlton.robogeddon.world.gen.BaseComponent;
import com.noahcharlton.robogeddon.world.gen.BaseComponentAsset;
import com.noahcharlton.robogeddon.world.gen.BaseComponentType;
import com.noahcharlton.robogeddon.world.gen.EnemyBaseGenerator;
import com.noahcharlton.robogeddon.world.io.XmlWriter;
import com.noahcharlton.robogeddon.world.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Commands {

    public static void preInit() {
        register("give_items", Commands::giveItem);
        register("kill", Commands::killAll);
        register("spawn_base", Commands::spawnBase);
        register("base_test", Commands::spawnTest);
        register("base_comp_new", Commands::newBaseComp);
        register("base_comp_save", Commands::saveBaseComp);
    }

    private static void saveBaseComp(ServerWorld world, List<Argument> arguments) {
        FileHandle handle = GameData.getChild("base_comp.xml");
        XmlWriter writer = new XmlWriter(handle);

        BaseComponentAsset.save(writer, world);

        writer.flush();
        writer.close();
    }

    private static void newBaseComp(ServerWorld world, List<Argument> arguments) {
        clearOriginChunk(world);

        for(int x = 15; x <= 32; x++) {
            for(int y = 15; y <= 32; y++) {
                var tile = world.getTileAt(x, y);
                tile.setBlock(null, true);
                tile.setUpperFloor(null, false);

                if(x == 15 || y == 15 || x == 32 || y == 32) {
                    tile.setFloor(Floors.grass, false);
                }
            }
        }
    }

    private static void spawnTest(ServerWorld world, List<Argument> arguments) {
        spawnBase(world, List.of(new Argument("ARTILLERY"), new Argument("turrets_1")));
    }

    private static void spawnBase(ServerWorld world, List<Argument> arguments) {
        BaseComponentType type = arguments.get(0).asEnum(BaseComponentType.class);
        String name = arguments.get(1).asString();
        Chunk chunk = world.getChunkAt(0, 0);
        EnemyBaseGenerator generator = world.getGenerator().getBaseGenerator();

        clearOriginChunk(world);
        for(BaseComponent comp : type.getComponents()) {
            if(name.equals(comp.getName())) {
                Log.info("Spawning base: " + type + ":" + name);

                applyComponent(chunk, comp, generator);
                return;
            }
        }

    }

    private static void clearOriginChunk(ServerWorld world) {
        for(int x = -1; x < Chunk.SIZE + 1; x++){
            for(int y = -1; y < Chunk.SIZE + 1; y++){
                var tile = world.getTileAt(x, y);
                tile.setFloor(Floors.sand, false);
                tile.setUpperFloor(null, true);

                if(tile.hasBlock())
                    world.destroyBlock(tile);

                if(x == -1 || y == -1 || x == 32 || y == 32) {
                    tile.setFloor(Floors.grass, false);
                }
            }
        }
    }

    private static void applyComponent(Chunk chunk, BaseComponent component, EnemyBaseGenerator generator) {
        for(IntermediateDirection direction : IntermediateDirection.values()){
            generator.applyComponent(direction, component, chunk);
        }

        for(int x = 0; x < Chunk.SIZE; x++){
            for(int y = 0; y < Chunk.SIZE; y++){
                chunk.getTile(x, y).markDirty();
            }
        }
    }

    private static void killAll(ServerWorld world, List<Argument> arguments) {
        for(Entity entity : world.getEntities()){
            if(!(entity instanceof RobotEntity)){
                entity.setDead(true);
            }
        }
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
            world.sendMessageToClient(new ChatMessage("[RED][[PARSE ERROR][WHITE]  " + getMessage(e)));
        }catch(RuntimeException e){
            world.sendMessageToClient(new ChatMessage("[RED][[INTERNAL ERROR][WHITE]  " + getMessage(e)));

            Log.error("Internal Error while executing command: " + text, e);
        }
    }

    public static String getMessage(Exception e) {
        return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
    }
}
