package com.fpoly.pro1121.adminapp.model;

public class ProductOrder {
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
}
