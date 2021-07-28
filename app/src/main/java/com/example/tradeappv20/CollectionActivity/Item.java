package com.example.tradeappv20.CollectionActivity;

import com.google.android.gms.common.util.Strings;

public class Item {
    private String id;
    private String item_name;
    private String owner;
    private String item_description;
    private String imageURL;
    private String status;
    private String for_free;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String isTrade;

    public String getIsTrade() {
        return isTrade;
    }

    public void setIsTrade(String isTrade) {
        this.isTrade = isTrade;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTag4() {
        return tag4;
    }

    public void setTag4(String tag4) {
        this.tag4 = tag4;
    }

    public String getTag5() {
        return tag5;
    }

    public void setTag5(String tag5) {
        this.tag5 = tag5;
    }


    public String getFor_free() {
        return for_free;
    }

    public void setFor_free(String for_free) {
        this.for_free = for_free;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Item(String id, String item_name, String imageURL, String status, String owner, String item_description, String for_free, String isTrade) {
        this.id = id;
        this.item_name = item_name;
        this.imageURL = imageURL;
        this.status = status;
        this.owner = owner;
        this.for_free = for_free;
        this.item_description = item_description;
        this.isTrade = isTrade;
    }

    public Item() {

    }
}
