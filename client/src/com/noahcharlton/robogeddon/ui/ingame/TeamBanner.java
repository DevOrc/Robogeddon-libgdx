package com.noahcharlton.robogeddon.ui.ingame;

import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Padding;
import com.noahcharlton.robogeddon.world.ClientWorld;

public class TeamBanner extends Padding {

    private final NinePatchBackground background = new NinePatchBackground(UIAssets.teamBanner);
    private final Label label;

    public TeamBanner() {
        super(new Label());

        this.label = (Label) getWidget();

        all(15);
        left(25);
        right(25);

        setBackground(background);
    }

    @Override
    public void update() {
        ClientWorld world = client.getWorld();

        if(world != null && world.getPlayersRobot() != null){
            Entity player = world.getPlayersRobot();

            label.setText("Team: " + player.getTeam().getDisplayName());
            background.setColor(player.getTeam().getColor());
        }
    }
}
