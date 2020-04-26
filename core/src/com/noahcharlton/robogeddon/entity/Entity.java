package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.Objects;

public class Entity {

    public static final int DEFAULT_ID = -1;

    private final EntityType type;
    protected final World world;
    protected final Team team;

    private int id = DEFAULT_ID;
    protected float x;
    protected float y;
    protected float angle;
    protected float velocity;
    protected float angularVelocity;

    private int health;

    private boolean isDead;
    private boolean dirty;

    public Entity(EntityType type, World world, Team team) {
        this.team = team;
        this.type = type;
        this.world = world;
        this.dirty = world.isServer();
        this.health = type.getHealth();
    }

    public void onCustomMessageReceived(CustomEntityMessage message) {
        Log.warn("Unhandled message: " + message.getClass().getName());
    }

    @Side(Side.SERVER)
    public void damage(int amount){
        if(world.isClient()){
            throw new UnsupportedOperationException("Entity cannot be damaged on the client.");
        }

        health -= amount;
        dirty = true;
    }

    public final void onUpdate(){
        update();
        updateKinematics();

        if(health <= 0)
            isDead = true;

        if(dirty && world.isServer())
            sendUpdatedValues();
    }

    private void sendUpdatedValues() {
        var message = createUpdateMessage();

        world.sendMessageToClient(message);
    }

    public EntityUpdateMessage createUpdateMessage() {
        return new EntityUpdateMessage(id, x, y, angle, velocity, angularVelocity, health);
    }

    public void onUpdateMessage(EntityUpdateMessage message) {
        health = message.getHealth();

        x = message.getX();
        y = message.getY();
        angle = message.getAngle();
        velocity = message.getVelocity();
        angularVelocity = message.getAngularVelocity();
    }

    protected void update(){}

    public void updateKinematics(){
        if(Math.abs(velocity) < .001 && Math.abs(angularVelocity) < .001){
            return;
        }

        x += velocity * Math.cos(angle);
        y += velocity * Math.sin(angle);
        angle += angularVelocity;
        dirty = true;
    }

    public boolean isInWorld(){
        return world.tileFromPixel(x, y) != null;
    }

    protected Vector2 createVectorBetween(Entity other){
        return new Vector2(other.x, other.y).sub(x, y);
    }

    public void onSave(XmlWriter xml){}

    public void onLoad(Element element){ }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return getId() == entity.getId() &&
                Objects.equals(getType(), entity.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getId());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setId(int id) {
        if(this.id != DEFAULT_ID){
            throw new IllegalStateException("This entity already has an ID: " + this.id);
        }
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    protected void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public Team getTeam() {
        return team;
    }
}
