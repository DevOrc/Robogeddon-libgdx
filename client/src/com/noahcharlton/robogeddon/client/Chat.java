package com.noahcharlton.robogeddon.client;

import com.noahcharlton.robogeddon.message.ChatMessage;

import java.util.LinkedList;

public class Chat {

    private static final int MAX_SIZE = 20;

    private LinkedList<String> chats = new LinkedList<>();
    private long lastMessageTime;

    public Chat() {
        addMessage("Welcome to chat!");
    }

    public void handleMessage(ChatMessage message) {
        addMessage(message.getText());
    }

    public void addMessage(String message){
        resetLastTime();

        chats.add(message);

        while(chats.size() > MAX_SIZE){
            chats.pop();
        }
    }

    public String[] getText() {
        return chats.toArray(new String[0]);
    }

    public void resetLastTime() {
        lastMessageTime = System.currentTimeMillis();
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        for(String s : getText()){
            builder.append(s);
            builder.append("\n");
        }

        return builder.toString();
    }
}
