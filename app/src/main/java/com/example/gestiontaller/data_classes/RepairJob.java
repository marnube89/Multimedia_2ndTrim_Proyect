package com.example.gestiontaller.data_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RepairJob implements Serializable {
    private String repairNumber;
    private String car;
    private String startDate; //To later be change to the correct format
    private String finishDate; //To later be change to the correct format
    private String chiefMechanic;
    private String description;
    private String diagnostic;
    private ArrayList<String> mechanics;
    private ArrayList<String> repairTasks;

    private Boolean isFinished;

    public RepairJob() {
    }

    public RepairJob(String repairNumber, String car, String startDate) {
        this.repairNumber = repairNumber;
        this.car = car;
        this.startDate = startDate;
        this.mechanics = new ArrayList<>();
        this.repairTasks = new ArrayList<>();
    }

    public RepairJob(String repairNumber, String car, String startDate, String chiefMechanic, String description) {
        this.repairNumber = repairNumber;
        this.car = car;
        this.startDate = startDate;
        this.chiefMechanic = chiefMechanic;
        this.diagnostic = "";
        this.mechanics = new ArrayList<>();
        this.repairTasks = new ArrayList<>();
        this.description = description;
    }

    public RepairJob(HashMap<String, Object> map){
        this.repairNumber = (String) map.get("repairNumber");
        this.car = (String) map.get("car");
        this.startDate = (String) map.get("startDate");
        this.chiefMechanic = (String) map.get("chiefMechanic");
        this.description = (String) map.get("description");
        if(map.containsKey("diagnostic")){
            this.diagnostic = (String) map.get("diagnostic");
        }else{
            this.diagnostic = "";
        }

        this.mechanics = new ArrayList<>();
        this.repairTasks = new ArrayList<>();
        if(map.get("isFinished")!=null){
            this.isFinished = (Boolean) map.get("isFinished");
        }else{
            this.isFinished = false;
        }
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepairNumber() {
        return repairNumber;
    }

    public void setRepairNumber(String repairNumber) {
        this.repairNumber = repairNumber;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getChiefMechanic() {
        return chiefMechanic;
    }

    public void setChiefMechanic(String chiefMechanic) {
        this.chiefMechanic = chiefMechanic;
    }

    public ArrayList<String> getMechanics() {
        return mechanics;
    }

    public void setMechanics(ArrayList<String> mechanics) {
        this.mechanics = mechanics;
    }

    public ArrayList<String> getRepairTasks() {
        return repairTasks;
    }

    public void setRepairTasks(ArrayList<String> repairTasks) {
        this.repairTasks = repairTasks;
    }
}
