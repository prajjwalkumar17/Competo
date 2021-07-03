package com.StartupBBSR.competo.Models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    String userName, userEmail, userPhoto, userPhone;
    String userBio;
    String userLinkedin;
    String userRole, organizerRole;
    String userID;
    List<String> userChips;
    List<String> userEvents;

    private Boolean selected;

    public UserModel() {
    }

    public UserModel(String userName, String userEmail, String userPhoto, String userPhone, String userBio, String userLinkedin, String userRole, String organizerRole, String userID, List<String> userChips) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
        this.userPhone = userPhone;
        this.userBio = userBio;
        this.userLinkedin = userLinkedin;
        this.userRole = userRole;
        this.organizerRole = organizerRole;
        this.userID = userID;
        this.userChips = userChips;
    }

    public String getOrganizerRole() {
        return organizerRole;
    }

    public void setOrganizerRole(String organizerRole) {
        this.organizerRole = organizerRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserLinkedin() {
        return userLinkedin;
    }

    public void setUserLinkedin(String userLinkedin) {
        this.userLinkedin = userLinkedin;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public List<String> getUserChips() {
        return userChips;
    }

    public void setUserChips(List<String> userChips) {
        this.userChips = userChips;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public List<String> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(List<String> userEvents) {
        this.userEvents = userEvents;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean isSelected() {
        return selected;
    }
}
