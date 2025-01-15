package com.example.gestiontaller.aux_classes;

import java.util.HashMap;

public class CarPart {
    private String retailerName;
    private Long unitsAvailable;
    private String name;
    private Long price;

    public CarPart() {
    }

    public CarPart(String retailerName, Long unitsAvailable, String name, Long price) {
        this.retailerName = retailerName;
        this.unitsAvailable = unitsAvailable;
        this.name = name;
        this.price = price;
    }
    public CarPart(HashMap<String, Object> map){
        this.retailerName = (String) map.get("retailerName");
        this.unitsAvailable = (Long) map.get("unitsAvailable");
        this.name = (String) map.get("name");
        this.price = (Long) map.get("price");
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public Long getUnitsAvailable() {
        return unitsAvailable;
    }

    public void setUnitsAvailable(Long unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
