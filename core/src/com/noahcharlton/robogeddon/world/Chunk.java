package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.floor.Floors;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.Objects;

public class Chunk {

    public static final int SIZE = 32;

    private final World world;
    private final GridPoint2 location;
    private final Tile[][] tiles = new Tile[SIZE][SIZE];

    private Team team = Team.NEUTRAL;

    @Side(Side.SERVER)
    Chunk(World world, GridPoint2 location) {
        this.location = location;
        this.world = world;

        for(int x = 0; x < SIZE; x++){
            for(int y = 0; y < SIZE; y++) {
                var index = getArrayIndex(x, y);
                var worldX = x + (location.x * 32);
                var worldY = y + (location.y * 32);

                tiles[index.x][index.y] = new Tile(world,  this, worldX, worldY);
                tiles[index.x][index.y].setFloor(Floors.dirt, false);
            }
        }
    }

    @Side(Side.CLIENT)
    public Chunk(World world, WorldSyncMessage message) {
        this.location = message.getChunk();
        this.world = world;
        this.team = message.getTeam();

        for(int x = 0; x < SIZE; x++){
            for(int y = 0; y < SIZE; y++) {
                var index = getArrayIndex(x, y);
                var worldX = x + (location.x * 32);
                var worldY = y + (location.y * 32);

                tiles[index.x][index.y] = new Tile(world,  this, worldX, worldY);
                tiles[index.x][index.y].onTileUpdate(message.getTiles()[index.x][index.y]);
            }
        }
    }

    /**
     * @returns the index the passed in tile should be placed on.
     * The indices are flipped on the negative side.
     *
     * Example: The tile (-1, 0) has the index 0 even though the origin of the chunk is at (-32, 0).
     * This is done so that the arrays are always going away from zero
     */
    private GridPoint2 getArrayIndex(int interX, int interY){
        GridPoint2 output = new GridPoint2();
        if(location.x >= 0){
            output.x = interX;
        }else{
            output.x = 31 - interX;
        }

        if(location.y >= 0){
            output.y = interY;
        }else{
            output.y = (31 - interY) % 32;
        }

         return output;
    }

    public void update() {
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                getTile(x, y).update();
            }
        }
    }

    @Side(Side.CLIENT)
    public void render(SpriteBatch batch, int layer) {
        if(layer == 0){
            for(int x = 0; x < SIZE; x++) {
                for(int y = 0; y < SIZE; y++) {
                    getTile(x, y).renderFloor(batch);
                }
            }

            for(int x = 0; x < SIZE; x++) {
                for(int y = 0; y < SIZE; y++) {
                    getTile(x, y).renderBlock(batch);
                }
            }
        }else{
            for(int x = 0; x < SIZE; x++) {
                for(int y = 0; y < SIZE; y++) {
                    var tile = getTile(x, y);

                    if(tile.hasBlock() && getTile(x, y).getBlock().getRenderer() != null){
                        getTile(x, y).getBlock().getRenderer().renderLayer(batch, tile, layer);
                    }
                }
            }
        }

    }

    void renderTeam() {
        var drawer = Core.client.getGameShapeDrawer();
        var x = getLocation().x * Chunk.SIZE * Tile.SIZE + 2;
        var y = getLocation().y * Chunk.SIZE * Tile.SIZE + 2;
        var size = Chunk.SIZE * Tile.SIZE - 4;

        int chunkX = getLocation().x;
        int chunkY = getLocation().y;
        drawer.setColor(team.getColor());
        drawer.setDefaultLineWidth(4);

        if(shouldDrawBorder(chunkX, chunkY + 1)){
            drawer.line(x, y + size, x + size, y + size);
        }
        if(shouldDrawBorder(chunkX, chunkY - 1)){
            drawer.line(x, y, x + size, y);
        }
        if(shouldDrawBorder(chunkX + 1, chunkY)){
            drawer.line(x + size, y, x + size, y + size);
        }
        if(shouldDrawBorder(chunkX - 1, chunkY)){
            drawer.line(x, y, x, y + size);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.F3)){
            var color = team.getColor().cpy();
            color.a = .4f;

            drawer.setColor(color);
            drawer.filledRectangle(x, y, size, size);
        }
    }

    private boolean shouldDrawBorder(int chunkX, int chunkY){
        var chunk = world.getChunkAt(chunkX, chunkY);

        if(chunk == null)
            return false;

        return chunk.getTeam() != team;
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= SIZE || y >= SIZE){
            return null;
        }

        return tiles[x][y];
    }

    public GridPoint2 getLocation() {
        return location;
    }

    public void setTeam(Team team) {
        this.team = Objects.requireNonNull(team);

        if(world.isServer())
            world.sendMessageToClient(new ChunkTeamUpdateMessage(this));
    }

    public Team getTeam() {
        return team;
    }
}
