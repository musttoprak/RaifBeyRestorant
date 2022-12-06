package com.example.raifbeyrestaurant.models;

public class Purchase {


    String id;
    String uid;


    String displayName;
    String products;
    String productsPrice;
    Boolean confirmation;

    public Purchase(String id,String uid, String displayName, String products, String productsPrice,Boolean confirmation) {
        this.id=id;
        this.uid = uid;
        this.displayName = displayName;
        this.products = products;
        this.productsPrice = productsPrice;
        this.confirmation=confirmation;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getProductsPrice() {
        return productsPrice;
    }

    public void setProductsPrice(String productsPrice) {
        this.productsPrice = productsPrice;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }
}
