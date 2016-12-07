package com.narae.cafeorder.history;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 성열 on 2016-11-14.
 */

public class History {

    private String id;
    private int icon;
    private String title;
    private String subtitle;
    private List<HistoryItem> historyItems;

    public History() {
    }

    public History(String id, int icon, String title, String subtitle) {
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
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
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

    public List<HistoryItem> getHistoryItems() {
        return historyItems;
    }

    public void setHistoryItems(List<HistoryItem> historyItems) {
        this.historyItems = historyItems;
    }
    public void addHistoryItem(HistoryItem historyItem) {
        if(this.historyItems == null) {
            this.historyItems = new ArrayList<>();
        }
        this.historyItems.add(historyItem);
    }
}
