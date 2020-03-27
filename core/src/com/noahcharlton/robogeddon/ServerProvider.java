package com.noahcharlton.robogeddon;

public interface ServerProvider {

    String getName();

    void sendMessageToClient(Message message);

    Message getMessageFromClient();

}
