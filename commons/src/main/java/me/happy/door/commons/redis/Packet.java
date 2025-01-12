package me.happy.door.commons.redis;

import java.util.UUID;

public abstract class Packet {

    //TODO: Better way of sender and same
    private UUID sender;
    private boolean same;
    private String identifier;

    void onSend() {}

    public abstract void onReceive();

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public abstract String getIdentifier();
}