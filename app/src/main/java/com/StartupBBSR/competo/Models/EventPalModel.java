package com.StartupBBSR.competo.Models;

import java.io.Serializable;
import java.util.List;

public class EventPalModel implements Serializable {
    private String Name;
    private String Bio;
    private String Photo;
    private List<String> Chips;
    private String UserID;
    private String lastMessage;
    private boolean selected;

    public EventPalModel() {
    }

    public EventPalModel(String name, String bio, String photo, List<String> chips, String lastMessage) {
        Name = name;
        Bio = bio;
        Photo = photo;
        Chips = chips;
        this.lastMessage = lastMessage;
    }

    public EventPalModel(String name, String id, String bio, String photo, List<String> chips, String lastMessage) {
        Name = name;
        UserID = id;
        Bio = bio;
        Photo = photo;
        Chips = chips;
        this.lastMessage = lastMessage;
    }

    public String getName() {
        return Name;
    }

    public String getBio() {
        return Bio;
    }

    public String getPhoto() {
        return Photo;
    }

    public List<String> getChips() {
        return Chips;
    }

    public String getUserID() {
        return UserID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean isSelected() {
        return selected;
    }
}
