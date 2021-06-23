package com.StartupBBSR.competo.Models;

public class TeamMessageModel {
    String message, messageID, senderID, senderName;
    Long timestamp;

    public TeamMessageModel() {
    }

    public TeamMessageModel(String message, String messageID, String senderID, String senderName, Long timestamp) {
        this.message = message;
        this.messageID = messageID;
        this.senderID = senderID;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
