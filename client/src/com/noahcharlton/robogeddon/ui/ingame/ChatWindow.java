package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextField;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class ChatWindow extends Stack {

    private final TextField input = new TextField();
    private final Label chat = new Label();

    public ChatWindow() {
        chat.setEnableMarkup(true);
        chat.align(Align.left);
        input.setBackground(new ColorBackground(new Color(1f, 1f, 1f, 0f)));
        input.setOnEnter(this::sendChat);
        input.setMaximumLength(64);
        input.setPromptText("");
        input.align(Align.left);

        setBackground(new ColorBackground(new Color(0f, 0f, 0f, .25f)));
        add(chat.pad().all(10));
        add(input);
    }

    @Override
    public void update() {
        if(client.getWorld() != null){
            chat.setText(client.getWorld().getChat().toString());
        }

        if(hasInputFocus() && client.getWorld().isTutorial())
            client.getUi().setKeyFocus(null);

        var alpha = getCurrentAlpha();

        setBackground(new ColorBackground(new Color(0f, 0f, 0f, alpha / 4f)));
        chat.setTextColor(new Color(1f, 1f, 1f, alpha));
    }

    public float getCurrentAlpha(){
        if(client.getWorld() == null){
            return 0f;
        }else if(client.getWorld().isTutorial()){
            return 1f;
        }

        var lastTime = client.getWorld().getChat().getLastMessageTime();

        if(isMouseOver() || hasInputFocus()){
            client.getWorld().getChat().resetLastTime();
            return 1f;
        }else if(lastTime + 4000 > System.currentTimeMillis()){
            return 1f;
        }

        return MathUtils.clamp(500 / (float) (System.currentTimeMillis() - lastTime - 4000) - .25f, 0f, 1f);
    }

    @Override
    protected void onClick(ClickEvent event) {

    }

    public void setInputText(String text){
        input.setText(text);
    }

    private void sendChat() {
        var message = input.getText();

        if(message.isBlank() || message.isEmpty())
            return;

        input.setText("");

        if(message.startsWith("/"))
            client.getUi().setKeyFocus(null);

        if(client.getWorld() != null)
            client.getWorld().sendMessageToServer(new ChatMessage(message));
    }

    public void focusInput() {
        client.getUi().setKeyFocus(input);
    }

    public boolean hasInputFocus(){
        return client.getUi().getKeyFocus() == input;
    }

    @Override
    public Widget setVisible(boolean visible) {
        if(!visible && client.getUi().getKeyFocus() == input)
            client.getUi().setKeyFocus(null);

        return super.setVisible(visible);
    }
}
