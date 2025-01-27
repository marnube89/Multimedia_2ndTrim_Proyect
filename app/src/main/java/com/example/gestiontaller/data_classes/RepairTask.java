package com.example.gestiontaller.data_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RepairTask implements Serializable {
    private String taskNumber; //RepairJob number + taskNumber
    private String chiefMechanic;
    private ArrayList<String> mechanics;
    private boolean completed;
    private String startDate; //To later be change to the correct format
    private String finishDate; //To later be change to the correct format
    private String description;
    private String repairNumber;
    private String comment;

    public RepairTask() {
    }

    public RepairTask(String taskNumber, String chiefMechanic, String startDate, String desc, String repairNumber) {
        this.taskNumber = taskNumber;
        this.chiefMechanic = chiefMechanic;
        this.startDate = startDate;
        this.completed = false;
        this.description = desc;
        this.repairNumber = repairNumber;
    }

    public RepairTask(HashMap<String, Object> map){
        this.taskNumber = (String) map.get("taskNumber");
        this.chiefMechanic = (String) map.get("chiefMechanic");
        this.mechanics = (ArrayList<String>) map.get("mechanics");
        this.completed = (boolean) map.get("completed");
        this.startDate = (String) map.get("startDate");
        this.description = (String) map.get("description");
        if(map.get("finishDate")!=null){
            this.finishDate = (String) map.get("finishDate");
        }
        this.repairNumber = (String) map.get("repairNumber");
        this.comment = (String) map.get("comment");
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRepairNumber() {
        return repairNumber;
    }

    public void setRepairNumber(String repairNumber) {
        this.repairNumber = repairNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
}
