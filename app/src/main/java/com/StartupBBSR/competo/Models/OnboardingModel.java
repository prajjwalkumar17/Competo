package com.StartupBBSR.competo.Models;

public class OnboardingModel {
    String Title, Description;
    int image;

    public OnboardingModel(String title, String description, int image) {
        Title = title;
        Description = description;
        this.image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
