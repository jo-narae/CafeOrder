package com.narae.cafeorder.cart;

import android.graphics.drawable.Drawable;

/**
 * Created by existmaster on 2016. 11. 13..
 */

public class CartMenuListViewItem {

    private String id;
    private Drawable icon;
    private String title;
    private String sellCount ;

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

    public String getSellCount() {
        return sellCount;
    }

    public void setSellCount(String sellCount) {
        this.sellCount = sellCount;
    }
}
