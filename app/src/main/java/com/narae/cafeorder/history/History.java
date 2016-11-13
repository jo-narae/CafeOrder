package com.narae.cafeorder.history;

import android.graphics.drawable.Drawable;

/**
 * Created by 성열 on 2016-11-14.
 */

public class History {

    private String id;
    private Drawable icon;
    private String title;
    private String subtitle;

    public History() {
    }

    public History(String id, Drawable icon, String title, String subtitle) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
