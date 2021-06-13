package com.StartupBBSR.competo.Models;

public class MessageModel {
    String message, senderID;
    Long timestamp;


    public MessageModel() {
    }

    public MessageModel(String senderID, String message, Long timestamp) {
        this.senderID = senderID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String senderID, String message) {
        this.senderID = senderID;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
