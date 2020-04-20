package com.noahcharlton.robogeddon.ui.pause;

import com.badlogic.gdx.files.FileHandle;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.widget.*;
import com.noahcharlton.robogeddon.util.GameData;
import com.noahcharlton.robogeddon.world.io.SaveWorldMessage;

public class SaveMenu extends Stack {

    private long refreshTime = Long.MAX_VALUE;

    public SaveMenu() {
        setBackground(new NinePatchBackground(UIAssets.dialog));
        setVisible(false);
        setMinHeight(250);

        buildLayout();
    }

    @Override
    public void update() {
        if(refreshTime < System.currentTimeMillis()){
            buildLayout();
            refreshTime = Long.MAX_VALUE;
        }
    }

    private void buildLayout() {
        getChildren().clear();
        invalidateParent();

        for(FileHandle saveFile: GameData.getSaveFiles()){
            add(new PairLayout()
                    .setLeft(new Label(saveFile.nameWithoutExtension()).setWidth(250))
                    .setRight(new TextButton("Save").setOnClick((clickEvent, button) -> save(saveFile)))
                    .pad().all(10).top(0));
        }

        var textField = new TextField().setPromptText("File Name");
        add(new PairLayout()
            .setLeft(textField.setWidth(250))
            .setRight(new TextButton("Save").setOnClick((clickEvent, button) -> save(textField)))
            .pad().all(10).top(5));
    }

    private void save(FileHandle saveFile) {
        save(saveFile.nameWithoutExtension());
    }

    private void save(TextField textField) {
        save(textField.getText());
    }

    private void save(String name){
        refreshTime = System.currentTimeMillis() + 500;
        client.getWorld().sendMessageToServer(new SaveWorldMessage(name));
    }

    @Override
    public Widget setVisible(boolean visible) {
        if(visible){
            buildLayout();
        }

       super.setVisible(visible);

        return this;
    }
}
