package com.fpoly.pro1121.adminapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductOrder implements Parcelable {
    private int id;
    private String idUser;
    private String idProduct;
    private int priceProduct;
    private int quantity;
    private int unitPrice;


    public ProductOrder(String idUser, String idProduct, int priceProduct, int quantity) {
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.priceProduct = priceProduct;
        this.quantity = quantity;
    }

    public ProductOrder(int id, String idUser, String idProduct, int priceProduct, int quantity, int unitPrice) {
        this.id = id;
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.priceProduct = priceProduct;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    protected ProductOrder(Parcel in) {
        id = in.readInt();
        idUser = in.readString();
        idProduct = in.readString();
        priceProduct = in.readInt();
        quantity = in.readInt();
        unitPrice = in.readInt();
    }

    public static final Creator<ProductOrder> CREATOR = new Creator<ProductOrder>() {
        @Override
        public ProductOrder createFromParcel(Parcel in) {
            return new ProductOrder(in);
        }

        @Override
        public ProductOrder[] newArray(int size) {
            return new ProductOrder[size];
        }
    };

    public int getUnitPrice() {
        return this.priceProduct * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(idUser);
        parcel.writeString(idProduct);
        parcel.writeInt(priceProduct);
        parcel.writeInt(quantity);
        parcel.writeInt(unitPrice);
    }
}
