package com.example.gestiontaller;

import java.util.HashMap;

public class CarInShop {
    private String licensePlate;
    private String chiefMechanic;
    private String dateOfArrival; //To later be change to the correct format
    private String client;


    public CarInShop() {
    }

    public CarInShop(String licensePlate, String chiefMechanic, String dateOfArrival, String client) {
        this.licensePlate = licensePlate;
        this.chiefMechanic = chiefMechanic;
        this.dateOfArrival = dateOfArrival;
        this.client = client;
    }

    public CarInShop(HashMap<String, Object> map) {
        this.licensePlate = (String) map.get("licensePlate");
        this.chiefMechanic = (String) map.get("chiefMechanic");
        this.dateOfArrival = (String) map.get("dateOfArrival");
        this.client = (String) map.get("client");
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getChiefMechanic() {
        return chiefMechanic;
    }

    public void setChiefMechanic(String chiefMechanic) {
        this.chiefMechanic = chiefMechanic;
    }

    public String getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(String dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }
}
