package com.advse.universitybazaar.bean;

public class Item {

    private int itemId;
    private String name;
    private String description;
    private String buyerId;
    private String sellerId;
    private int price;

    public Item() {

    }

    public Item(int itemId, String name, String description, String buyerId, String sellerId, int price) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
