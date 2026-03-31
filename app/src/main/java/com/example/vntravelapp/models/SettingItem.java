package com.example.vntravelapp.models;

public class SettingItem {
    private final String title;
    private final String subtitle;
    private final int iconRes;

    public SettingItem(String title, String subtitle, int iconRes) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getIconRes() {
        return iconRes;
    }
}

