package com.noahcharlton.robogeddon.message;

public class ChatMessage implements Message {

    private final String text;

    public ChatMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
