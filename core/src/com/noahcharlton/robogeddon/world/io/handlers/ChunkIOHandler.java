package com.noahcharlton.robogeddon.world.io.handlers;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Multiblock;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.WorldIOHandler;
import com.noahcharlton.robogeddon.world.io.XmlWriter;
import com.noahcharlton.robogeddon.world.team.Team;

public class ChunkIOHandler implements WorldIOHandler {

    @Override
    public void save(XmlWriter xml, ServerWorld world) {
        world.getChunks().forEachRemaining(chunk -> saveChunk(chunk, xml));
    }

    private void saveChunk(Chunk chunk, XmlWriter xml) {
        var element = xml.element("Chunk")
                .attribute("x", chunk.getLocation().x)
                .attribute("y", chunk.getLocation().y)
                .attribute("team", chunk.getTeam().name());

        for(int x = 0; x < Chunk.SIZE; x++){
            for(int y = 0; y < Chunk.SIZE; y++){
                saveTile(chunk, element, x, y);
            }
        }

        element.pop();
    }

    private void saveTile(Chunk chunk, XmlWriter xml, int x, int y) {
        var tile = chunk.getTile(x, y);

        var element = xml.element("Tile")
                .attribute("x", tile.getX())
                .attribute("y", tile.getY())
                .element("Floor", tile.getFloor().getTypeID());

        if(tile.hasBlock())
            element.element("Block", tile.getBlock().getTypeID());

        if(!(tile.getBlock() instanceof Multiblock) && tile.getTileEntity() != null){
            var tileEntityElement = element.element("TileEntity");
            tile.getTileEntity().save(tileEntityElement);
            tileEntityElement.pop();
        }

        element.pop();
    }

    @Override
    public void load(Element xml, ServerWorld world) {
        xml.getChildrenByName("Chunk").forEach(chunkXml -> loadChunk(chunkXml, world));
    }

    private void loadChunk(Element chunkXml, ServerWorld world) {
        var chunkX = chunkXml.getIntAttribute("x");
        var chunkY = chunkXml.getIntAttribute("y");
        var team = chunkXml.getEnumAttribute("team", Team.class);
        world.createChunk(chunkX, chunkY, false).setTeam(team);

        chunkXml.getChildrenByName("Tile").forEach(tileXml -> loadTile(tileXml, world));
    }

    private void loadTile(Element tileXml, ServerWorld world) {
        var tileX = tileXml.getIntAttribute("x");
        var tileY = tileXml.getIntAttribute("y");
        var tile = world.getTileAt(tileX, tileY);
        var floor = tileXml.get("Floor");
        var block = tileXml.get("Block", null);

        tile.setFloor(Core.floors.get(floor), false);

        if(block == null)
            return;

        if(block.startsWith("multi,")) {
            var parts = block.substring(6).split(",", 3);
            var rootX = Integer.parseInt(parts[0]);
            var rootY = Integer.parseInt(parts[1]);
            var id = parts[2];

            tile.setBlock(new Multiblock(Core.blocks.get(block), rootX, rootY), false);
        }else{
            tile.setBlock(Core.blocks.get(block), false);
        }

        if(!(tile.getBlock() instanceof Multiblock) && tile.getTileEntity() != null){
            tile.getTileEntity().load(tileXml.getChildByName("TileEntity"));
        }
    }

    @Override
    public String getTypeID() {
        return "Chunks";
    }
}
