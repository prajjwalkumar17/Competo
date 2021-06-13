package com.StartupBBSR.competo.Models;

public class FragmentEventModel {
    String Title, Day, Month;
    Integer img;

    public FragmentEventModel(String title, String day, String month, Integer img) {
        Title = title;
        Day = day;
        Month = month;
        this.img = img;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }
}
