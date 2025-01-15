package com.example.gestiontaller;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    private String fullName;
    private String mail;
    private Long tlfNumber;
    private int jobRol;
    private String password;
    private String uid;
    private boolean activeAccount = true;

    public User() {
    }

    public User(String fullName, String mail, Long tlfNumber, int jobRol, String password) {
        this.fullName = fullName;
        this.mail = mail;
        this.tlfNumber = tlfNumber;
        this.jobRol = jobRol;
        this.password = password;
    }

    public User(HashMap<String, Object> map) {
        this.fullName = (String) map.get("fullName");
        this.jobRol = Integer.parseInt(map.get("jobRol").toString());
        this.mail = (String) map.get("mail");
        this.password = (String) map.get("password");
        this.tlfNumber = Long.parseLong(map.get("tlfNumber").toString());
        this.uid = (String) map.get("uid");
        if(map.get("activeAccount")!=null){
            this.activeAccount = false;
        }
    }

    public boolean isActiveAccount() {
        return activeAccount;
    }

    public void setActiveAccount(boolean activeAccount) {
        this.activeAccount = activeAccount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getTlfNumber() {
        return tlfNumber;
    }

    public void setTlfNumber(Long tlfNumber) {
        this.tlfNumber = tlfNumber;
    }

    public int getJobRol() {
        return jobRol;
    }

    public void setJobRol(int jobRol) {
        this.jobRol = jobRol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void getUser(HashMap<String, Object> map){
        this.fullName = (String) map.get("fullName");
        this.jobRol = Integer.parseInt(map.get("jobRol").toString());
        this.password = (String) map.get("password");
        this.tlfNumber = Long.parseLong(map.get("tlfNumber").toString());
        this.uid = (String) map.get("uid");
    }

    public String getJobName(){
        switch (jobRol){
            case 0:
                return "Cliente";
            case 1:
                return "Admin";
            case 2:
                return "Administrativo";
            case 3:
                return "Mecanico jefe";
            case 4:
                return "Mecanico";
            default:
                return "Error";
        }
    }
}
