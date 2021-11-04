package com.fpoly.pro1121.adminapp.model;

public class Product {
    private String id;
    private String urlImage;
    private String name;
    private int price;
    private String description;
    private String categoryID;

    public Product(String id, String urlImage, String name, int price, String description, String categoryID) {
        this.id = id;
        this.urlImage = urlImage;
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryID = categoryID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
