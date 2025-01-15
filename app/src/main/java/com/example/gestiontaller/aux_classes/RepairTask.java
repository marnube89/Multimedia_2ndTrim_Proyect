package com.example.gestiontaller.aux_classes;

import java.util.ArrayList;

public class RepairTask {
    private String taskNumber; //RepairJob number + taskNumber
    private User chiefMechanic;
    private ArrayList<User> mechanics;
    private boolean completed;
    private String startDate; //To later be change to the correct format
    private String finishDate; //To later be change to the correct format

    public RepairTask() {
    }

    public RepairTask(String taskNumber, User chiefMechanic, ArrayList<User> mechanics, String startDate) {
        this.taskNumber = taskNumber;
        this.chiefMechanic = chiefMechanic;
        this.mechanics = mechanics;
        this.startDate = startDate;
        this.completed = false;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public User getChiefMechanic() {
        return chiefMechanic;
    }

    public void setChiefMechanic(User chiefMechanic) {
        this.chiefMechanic = chiefMechanic;
    }

    public ArrayList<User> getMechanics() {
        return mechanics;
    }

    public void setMechanics(ArrayList<User> mechanics) {
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
