package com.juanuni.terapiappfrontend;

public class Product {
    private String title;
    private String price;
    private int imageResourceId;

    public Product(String title, String price, int imageResourceId) {
        this.title = title;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}