package com.noahcharlton.robogeddon.world.electricity;

import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.HashMap;
import java.util.Map;

public class PowerGraphUpdate implements Message {

    private Map<Team, ClientPowerGraph> powerGraphs = new HashMap<>();

    public PowerGraphUpdate(Map<Team, ServerPowerGraph> graphs) {
        for(var entry : graphs.entrySet()){
            powerGraphs.put(entry.getKey(), new ClientPowerGraph(entry.getValue()));
        }
    }

    public Map<Team, ClientPowerGraph> getPowerGraphs() {
        return powerGraphs;
    }
}
