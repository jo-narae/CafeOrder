package com.narae.cafeorder.menu;

import android.graphics.drawable.Drawable;

public class MenuListViewItem {
    private Drawable iconDrawable ;
    private String keyName ;
    private String engName ;
    private String korName ;
    private String price ;

    public Drawable getIcon() {
        return iconDrawable;
    }

    public void setIcon(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getKorName() {
        return korName;
    }

    public void setKorName(String korName) {
        this.korName = korName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
