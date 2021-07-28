package com.example.tradeappv20.Fragments;

public class Record {
    private String item_name;
    private String old_item_name;
    private String last_owner;
    private String new_owner;
    private String was_trade;
    private String old_item_uri;
    private String new_item_uri;

    public Record(String item_name, String last_owner, String new_owner, String was_trade, String new_item_uri) {
        this.item_name = item_name;
        this.last_owner = last_owner;
        this.new_owner = new_owner;
        this.was_trade = was_trade;
        this.new_item_uri = new_item_uri;
    }

    public Record(String item_name, String old_item_name, String last_owner, String new_owner, String was_trade, String new_item_uri, String old_item_uri) {
        this.item_name = item_name;
        this.old_item_name = old_item_name;
        this.last_owner = last_owner;
        this.new_owner = new_owner;
        this.was_trade = was_trade;
        this.new_item_uri = new_item_uri;
        this.old_item_uri = old_item_uri;
    }

    public Record (){

    }

    public String getOld_item_name() {
        return old_item_name;
    }

    public void setOld_item_name(String old_item_name) {
        this.old_item_name = old_item_name;
    }

    public String getOld_item_uri() {
        return old_item_uri;
    }

    public void setOld_item_uri(String old_item_uri) {
        this.old_item_uri = old_item_uri;
    }

    public String getNew_item_uri() {
        return new_item_uri;
    }

    public void setNew_item_uri(String new_item_uri) {
        this.new_item_uri = new_item_uri;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getLast_owner() {
        return last_owner;
    }

    public void setLast_owner(String last_owner) {
        this.last_owner = last_owner;
    }

    public String getNew_owner() {
        return new_owner;
    }

    public void setNew_owner(String new_owner) {
        this.new_owner = new_owner;
    }

    public String getWas_trade() {
        return was_trade;
    }

    public void setWas_trade(String was_trade) {
        this.was_trade = was_trade;
    }
}
