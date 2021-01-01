package com.app;

public class ListItem {
    private String titleList;
    private String listID;

    public ListItem(){}
    public ListItem(String titleList) {
        this.titleList = titleList;
    }

    public void setTitleList(String titleList) {
        this.titleList = titleList;
    }

    public String getTitleList() {
        return titleList;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }
}
