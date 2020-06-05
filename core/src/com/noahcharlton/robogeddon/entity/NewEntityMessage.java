package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.team.Team;

public class NewEntityMessage implements Message {

    private final EntityUpdateMessage info;
    private final String entityType;
    private final int ID;
    private final Team team;

    public NewEntityMessage(Entity entity) {
        this.entityType = entity.getType().getTypeID();
        this.ID = entity.getId();
        this.team = entity.getTeam();
        this.info = entity.createUpdateMessage();
    }

    public int getID() {
        return ID;
    }

    public String getEntityType() {
        return entityType;
    }

    public Team getTeam() {
        return team;
    }

    public EntityUpdateMessage getInfo() {
        return info;
    }
}
