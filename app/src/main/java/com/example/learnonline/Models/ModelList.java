package com.example.learnonline.Models;

import java.util.ArrayList;

public class ModelList {
    private String header_title;
   private ArrayList<ItemData> listItem;
   // private String description,picc,timestamp,title,type,userid;

    public ModelList() {
    }

    public ModelList(String header_title,ArrayList<ItemData> listItem) {
        this.header_title = header_title;
        this.listItem = listItem;
    }

    public String getHeader_title() {
        return header_title;
    }

    public void setHeader_title(String header_title) {
        this.header_title = header_title;
    }

    public ArrayList<ItemData> getListItem() {
        return listItem;
    }

    public void setListItem(ArrayList<ItemData> listItem) {
        this.listItem = listItem;
    }
}
