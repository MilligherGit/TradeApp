package com.example.tradeappv20.ChatActivity;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String trade;
    private String item_id;
    private String item_name;
    private String anoser_item_id;
    private String anoser_item_name;
    private String id;
    private String exist;

    public Chat(String id, String sender, String receiver, String message, String trade, String item_id, String anoser_item_name, String anoser_item_id, String item_name, String exist) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.trade = trade;
        this.item_id = item_id;
        this.anoser_item_name = anoser_item_name;
        this.anoser_item_id = anoser_item_id;
        this.item_name = item_name;
        this.exist = exist;
    }

    public Chat() {
    }

    public String getExist() {
        return exist;
    }

    public void setExist(String exist) {
        this.exist = exist;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getAnoser_item_id() {
        return anoser_item_id;
    }

    public void setAnoser_item_id(String anoser_item_id) {
        this.anoser_item_id = anoser_item_id;
    }

    public String getAnoser_item_name() {
        return anoser_item_name;
    }

    public void setAnoser_item_name(String anoser_item_name) {
        this.anoser_item_name = anoser_item_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }
}
