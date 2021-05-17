package com.StartupBBSR.competo.Models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    String userName, userEmail, userPhoto, userPhone;
    String userBio;
    String userLinkedin;
    String userRole;
    List<String> userChips;

    public String getOrganizerRole() {
        return organizerRole;
    }

    public void setOrganizerRole(String organizerRole) {
        this.organizerRole = organizerRole;
    }

    String organizerRole;

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
}
