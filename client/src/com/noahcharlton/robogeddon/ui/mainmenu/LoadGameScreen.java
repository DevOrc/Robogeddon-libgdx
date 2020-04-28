package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.PairLayout;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.util.GameData;
import com.noahcharlton.robogeddon.world.settings.SavedWorldSettings;

public class LoadGameScreen extends MainMenuSubScreen {

    private final Stack centerPane = new Stack();

    public LoadGameScreen() {
        super("Load Game");
        buildCenterPane();

        add(centerPane).align(Align.center);
    }

    private void buildCenterPane() {
        centerPane.getChildren().clear();
        invalidate();

        for(FileHandle file: GameData.getSaveFiles()){
            centerPane.add(new PairLayout()
                    .setLeft(new Label(file.nameWithoutExtension()).setWidth(250))
                    .setRight(new TextButton("Load").setOnClick((clickEvent, button) -> load(file)).pad().right(100)))
                    .pad().all(10).top(0);
        }

        centerPane.add(new TextButton("Refresh")
                .setOnClick((clickEvent, button) -> Gdx.app.postRunnable(this::buildCenterPane))
                .pad().top(30).setMaxWidth(100));
    }

    private void load(FileHandle file) {
        client.startGame(new SavedWorldSettings(file.nameWithoutExtension()));
    }
}
