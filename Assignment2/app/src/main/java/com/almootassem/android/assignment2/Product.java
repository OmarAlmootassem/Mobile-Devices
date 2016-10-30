package com.almootassem.android.assignment2;

/**
 * Created by 100520286 on 10/30/2016.
 */

public class Product {

    private int productId;
    private String name;
    private String description;
    private double price;

    public Product(int productId, String name, String description, double price){
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product( String name, String description, double price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getProductId(){
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
