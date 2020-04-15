package com.noahcharlton.robogeddon.world.io.handlers;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.EntityUpdateMessage;
import com.noahcharlton.robogeddon.message.MessageSerializer;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.WorldIOHandler;
import com.noahcharlton.robogeddon.world.io.XmlWriter;
import com.noahcharlton.robogeddon.world.team.Team;

public class EntityIOHandler implements WorldIOHandler {

    @Override
    public void save(XmlWriter xml, ServerWorld world) {
        world.getEntities().forEach(entity -> {
            if(entity.getType() != EntityType.robotEntity){ //TODO: Save players in Singleplayer
                saveEntity(xml.element("Entity"), entity);
            }
        });

        xml.element("LastEntityID", world.getLastEntityID());
    }

    private void saveEntity(XmlWriter xml, Entity entity) {
        xml.attribute("id", entity.getId());
        xml.attribute("team", entity.getTeam().name());
        xml.attribute("type", entity.getType().getTypeID());
        xml.element("Data", MessageSerializer.messageToString(entity.createUpdateMessage()));
        entity.onSave(xml);

        xml.pop();
    }

    @Override
    public void load(Element xml, ServerWorld world) {
        world.setLastEntityID(xml.getInt("LastEntityID"));

        for(Element child : xml.getChildrenByName("Entity")){
            world.addEntity(loadEntity(child, world));
        }
    }

    private Entity loadEntity(Element xml, ServerWorld world) {
        var id = xml.getIntAttribute("id");
        var typeID = xml.getAttribute("type");
        var team = xml.getEnumAttribute("team", Team.class);
        var updateValues = getUpdateValues(xml);

        var entity = Core.entities.get(typeID).create(world, team);
        entity.setId(id);
        entity.onUpdateMessage(updateValues);
        entity.onLoad(xml);

        return entity;
    }

    private EntityUpdateMessage getUpdateValues(Element xml) {
        var text = xml.getChildByName("Data").getText();

        return (EntityUpdateMessage) MessageSerializer.parse(text);
    }

    @Override
    public String getTypeID() {
        return "Entities";
    }
}
