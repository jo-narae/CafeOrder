package com.narae.cafeorder.best;

import android.graphics.drawable.Drawable;

public class BestMenuListViewItem {

    private String id;
    private Drawable rank;
    private Drawable icon;
    private String title;
    private String sellCount ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Drawable getRank() {
        return rank;
    }

    public void setRank(Drawable rank) {
        this.rank = rank;
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

    public String getSellCount() {
        return sellCount;
    }

    public void setSellCount(String sellCount) {
        this.sellCount = sellCount;
    }
}
