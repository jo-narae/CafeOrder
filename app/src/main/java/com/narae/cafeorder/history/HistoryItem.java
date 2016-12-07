package com.narae.cafeorder.history;

/**
 * Created by 성열 on 2016-11-14.
 */

public class HistoryItem {
    private String menuname;
    private String count;
    private String detail;

    public HistoryItem() {
    }

    public HistoryItem(String menuname, String detail) {
        this.menuname = menuname;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
