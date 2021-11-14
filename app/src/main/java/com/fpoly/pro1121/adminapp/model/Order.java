package com.fpoly.pro1121.adminapp.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private String userID;
    private List<ProductOrder> productOrderList;
    private int unitPrice;
    private Date date;
//    public Order(String id, String userID, List<ProductOrder> productOrderList, int unitPrice) {
//        this.id = id;
//        this.userID = userID;
//        this.productOrderList = productOrderList;
//        this.unitPrice = unitPrice;
//    }

    public Order(String id, String userID, List<ProductOrder> productOrderList, int unitPrice, Date date) {
        this.id = id;
        this.userID = userID;
        this.productOrderList = productOrderList;
        this.unitPrice = unitPrice;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrder> productOrderList) {
        this.productOrderList = productOrderList;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
}
