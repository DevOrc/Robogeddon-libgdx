package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.PairLayout;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.TextField;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class ChatWindow extends Stack {

    private final TextField input = new TextField();
    private final TextButton send = new TextButton("Send");
    private final Label chat = new Label();

    public ChatWindow() {
        chat.setEnableMarkup(true);
        chat.align(Align.left);
        chat.setMinSize(350, 350);
        input.setOnEnter(() -> sendChat(null, null));
        send.setOnClick(this::sendChat);

        setMinHeight(350);
        setVisible(false);
        setBackground(new NinePatchBackground(UIAssets.dialog));
        add(chat.pad().all(10));
        add(new PairLayout()
                .setLeft(input.setMinWidth(350))
                .setRight(send)
                .pad().all(10));
    }

    @Override
    public void update() {
        if(client.getWorld() != null){
            chat.setText(client.getWorld().getChat().toString());
        }
    }

    @Override
    protected void onClick(ClickEvent event) {
        if(UIAssets.isEventOnDialogCloseButton(this, event)){
            setVisible(false);
        }
    }

    public void setInputText(String text){
        input.setText(text);
    }

    private void sendChat(ClickEvent clickEvent, Button button) {
        var message = input.getText();

        if(message.isBlank() || message.isEmpty())
            return;

        input.setText("");
        if(client.getWorld() != null)
            client.getWorld().sendMessageToServer(new ChatMessage(message));
    }

    public void focusInput() {
        client.getUi().setKeyFocus(input);
    }

    @Override
    public Widget setVisible(boolean visible) {
        if(!visible && client.getUi().getKeyFocus() == input)
            client.getUi().setKeyFocus(null);

        return super.setVisible(visible);
    }
}
