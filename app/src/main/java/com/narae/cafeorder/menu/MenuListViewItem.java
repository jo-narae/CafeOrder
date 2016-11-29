package com.narae.cafeorder.menu;

import android.graphics.drawable.Drawable;

/**
 * Created by existmaster on 2016. 11. 13..
 */

public class MenuListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private String price ;

    public Drawable getIcon() {
        return iconDrawable;
    }

    public void setIcon(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getTitle() {
        return titleStr;
    }

    public void setTitle(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getDesc() {
        return descStr;
    }

    public void setDesc(String descStr) {
        this.descStr = descStr;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
