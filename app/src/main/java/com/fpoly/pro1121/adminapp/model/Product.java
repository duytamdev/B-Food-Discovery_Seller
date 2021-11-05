package com.fpoly.pro1121.adminapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
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

    public void updateProduct(String urlImage, String name, int price, String description, String categoryID) {
        this.urlImage = urlImage;
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryID = categoryID;
    }

    protected Product(Parcel in) {
        id = in.readString();
        urlImage = in.readString();
        name = in.readString();
        price = in.readInt();
        description = in.readString();
        categoryID = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(urlImage);
        parcel.writeString(name);
        parcel.writeInt(price);
        parcel.writeString(description);
        parcel.writeString(categoryID);
    }
}
