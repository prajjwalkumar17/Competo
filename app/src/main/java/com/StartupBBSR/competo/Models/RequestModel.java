package com.StartupBBSR.competo.Models;

import java.io.Serializable;

// this is for each request
public class RequestModel implements Serializable {
    private String senderID;
    private String requestMessage;
    private Long timestamp;

    public RequestModel() {
    }

    public RequestModel(String senderID, String requestMessage, Long timestamp) {
        this.senderID = senderID;
        this.requestMessage = requestMessage;
        this.timestamp = timestamp;
    }

    public RequestModel(String senderID, String requestMessage) {
        this.senderID = senderID;
        this.requestMessage = requestMessage;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
