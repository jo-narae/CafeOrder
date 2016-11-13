package com.narae.cafeorder.history;

/**
 * Created by 성열 on 2016-11-14.
 */

public class HistoryItem {
    private String menuname;
    private String count;
    private String price;

    public HistoryItem() {
    }

    public HistoryItem(String menuname, String count, String price) {
        this.menuname = menuname;
        this.count = count;
        this.price = price;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
