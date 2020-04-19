package com.noahcharlton.robogeddon.ui.pause;

import com.badlogic.gdx.files.FileHandle;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.widget.*;
import com.noahcharlton.robogeddon.util.GameData;

public class SaveMenu extends Stack {

    public SaveMenu() {
        setBackground(new NinePatchBackground(UIAssets.dialog));
        setVisible(false);
        setMinHeight(250);

        buildLayout();
    }

    public void buildLayout() {
        for(FileHandle saveFile: GameData.getSaveFiles()){
            add(new PairLayout()
                    .setLeft(new Label(saveFile.nameWithoutExtension()).setWidth(250))
                    .setRight(new TextButton("Save"))
                    .pad().all(10).top(0));
        }

        add(new PairLayout()
            .setLeft(new TextField().setPromptText("File Name").setWidth(250))
            .setRight(new TextButton("Save"))
            .pad().all(10).top(5));
    }
}
