package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.util.Selectable;

public class SelectableMenu extends Stack {

    private final Label title = new Label();
    private final Label desc = new Label().setTextColor(Color.LIGHT_GRAY);
    private final Label details = new Label().setTextColor(Color.LIGHT_GRAY);
    private final Button button = new TextButton("Block Options").setOnClick((event, ui) ->
            Gdx.app.postRunnable(this::clickSubMenuButton));

    private final InGameScene mainScene;

    private Selectable selectable;

    public SelectableMenu(InGameScene scene) {
        this.mainScene = scene;

        setBackground(new ColorBackground());
        setMinSize(250, 100);

        add(title.pad().top(10).bottom(15));
        add(desc.pad().bottom(10));
        add(details.pad().bottom(10));
        add(button.pad().bottom(10).left(30).right(30));
    }

    @Override
    public void update() {
        if(this.selectable != client.getProcessor().getSelectable()){
            selectable = client.getProcessor().getSelectable();
            Gdx.app.postRunnable(mainScene::closeSelectableSubMenu);
            setMinSize(250, 100);
            setSize(250, 100);
            invalidate();
        }

        if(this.selectable != null && this.selectable.isInfoInvalid()){
            invalidate();
        }
    }

    @Override
    public void layout() {
        if(this.selectable == null)
            return;

        this.selectable.onInfoValidated();
        title.setText(selectable.getTitle());
        desc.setText(selectable.getDesc());
        details.setText(formatDebugInfo());
        button.setVisible(selectable.getSubMenuID() != null);

        super.layout();
    }

    private String formatDebugInfo() {
        StringBuilder builder = new StringBuilder();

        for(String string : selectable.getDetails()){
            builder.append(string);
            builder.append('\n');
        }

        return builder.toString();
    }

    private void clickSubMenuButton() {
        mainScene.toggleSelectableSubMenu(selectable);
    }
}
