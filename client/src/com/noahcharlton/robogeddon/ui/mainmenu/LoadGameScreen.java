package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.*;
import com.noahcharlton.robogeddon.util.GameData;
import com.noahcharlton.robogeddon.world.settings.SavedWorldSettings;

public class LoadGameScreen extends Scene {

    private final Stack centerPane = new Stack();

    public LoadGameScreen() {
        var title = new Label().setText("Load Game").setFont(UIAssets.largeFont).pad().top(50);
        var backButton = new TextButton("Back").setOnClick(this::toMainMenu).pad().bottom(100);
        buildCenterPane();

        add(centerPane).align(Align.center);
        add(title).align(Align.top);
        add(backButton).align(Align.bottom);
        setBackground(new ColorBackground(Color.BLACK));
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

    private void toMainMenu(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }
}
