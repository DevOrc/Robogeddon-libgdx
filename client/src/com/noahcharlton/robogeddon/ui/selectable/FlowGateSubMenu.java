package com.noahcharlton.robogeddon.ui.selectable;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.duct.ItemDuctTileEntity;
import com.noahcharlton.robogeddon.block.gate.FlowGateBlock;
import com.noahcharlton.robogeddon.block.gate.FlowGateSelectionMessage;
import com.noahcharlton.robogeddon.block.gate.FlowGateTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.TextureWidget;
import com.noahcharlton.robogeddon.util.Direction;

public class FlowGateSubMenu extends SelectableSubMenu{

    private static final float WIDTH = 300;
    private static final float HEIGHT = 200;

    private final TextureWidget icon = new TextureWidget()
            .setTexture(((FlowGateBlock) Blocks.flowGate).getTexture());

    private final TextButton north = new TextButton("Normal");
    private final TextButton east = new TextButton("Normal");
    private final TextButton south = new TextButton("Normal");
    private final TextButton west = new TextButton("Normal");

    public FlowGateSubMenu(String id) {
        super(id);

        north.setOnClick(this::onButtonClick);
        east.setOnClick(this::onButtonClick);
        south.setOnClick(this::onButtonClick);
        west.setOnClick(this::onButtonClick);
        add(north, east, south, west, icon);
    }

    @Override
    public void layout() {
        float centerX = getX() + (getWidth() / 2f);
        float centerY = getY() + (getHeight() / 2f);

        icon.setPosition(centerX - (icon.getWidth() / 2f), centerY - (icon.getHeight() / 2f));
        north.setPosition(centerX - (north.getWidth() / 2f), centerY + 40);
        south.setPosition(centerX - (south.getWidth() / 2f), centerY - south.getHeight() - 40);
        east.setPosition(centerX + 30, centerY - (east.getHeight() / 2f));
        west.setPosition(centerX - west.getWidth() - 30, centerY - (west.getHeight() / 2f));
        setSize(WIDTH, HEIGHT);
    }

    private void onButtonClick(ClickEvent clickEvent, Button button) {
        var textButton = (TextButton) button;

        if(textButton.getText().equals("Normal")){
            textButton.setText("Overflow");
        }else{
            textButton.setText("Normal");
        }

        sendSelection();
    }

    private void sendSelection() {
        var north = this.north.getText().equals("Overflow");
        var east = this.east.getText().equals("Overflow");
        var south = this.south.getText().equals("Overflow");
        var west = this.west.getText().equals("Overflow");
        var message = new FlowGateSelectionMessage(getTileEntity().getRootTile(), north, east, south, west);

        client.getWorld().sendMessageToServer(message);
        update(); //After sending the selection, set the text back to the server state
    }

    @Override
    public void update() {
        var tileEntity = getTileEntity();

        if(tileEntity == null)
            return;

        var flowGate = (FlowGateTileEntity) tileEntity;

        updateButtonState(flowGate);
        updateButtonVisibility(flowGate);
    }

    private void updateButtonState(FlowGateTileEntity flowGate) {
        north.setText(flowGate.isOverflowNorth() ? "Overflow" : "Normal");
        east.setText(flowGate.isOverflowEast() ? "Overflow" : "Normal");
        south.setText(flowGate.isOverflowSouth() ? "Overflow" : "Normal");
        west.setText(flowGate.isOverflowWest() ? "Overflow" : "Normal");
    }

    private void updateButtonVisibility(TileEntity tileEntity) {
        var tile = tileEntity.getRootTile();
        var north = tile.getNeighbor(Direction.NORTH).getTileEntity();
        var east = tile.getNeighbor(Direction.EAST).getTileEntity();
        var south = tile.getNeighbor(Direction.SOUTH).getTileEntity();
        var west = tile.getNeighbor(Direction.WEST).getTileEntity();

        checkButton(north, this.north, Direction.SOUTH);
        checkButton(east, this.east, Direction.WEST);
        checkButton(south, this.south, Direction.NORTH);
        checkButton(west, this.west, Direction.EAST);
    }

    private void checkButton(TileEntity tileEntity, TextButton button, Direction direction) {
        if(tileEntity instanceof ItemDuctTileEntity){
            var pullingOut = ((ItemDuctTileEntity) tileEntity).getDirection() != direction;

            button.setVisible(pullingOut);
        }else{
            button.setVisible(false);
        }
    }
}
