package com.example.webstore.backend.api.model;

import com.example.webstore.backend.model.Inventory;

public class ProductRequest {

    private String name;
    private String shortDescription;
    private String longDescription;
    private Double price;
    private Inventory inventory;

    public ProductRequest() {
    }

    public ProductRequest(String name, String shortDescription, String longDescription, Double price, Inventory inventory) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.price = price;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
