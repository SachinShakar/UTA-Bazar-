package com.advse.universitybazaar.bean;

/**
 * Created by shahsk0901 on 2/14/18.
 */

public class Student {
    private String mavID;
    private String name;
    private String password;
    private String email;
    private int clubId;
    private String type;

    public Student() {

    }

    public Student(String mavId, String name,int clubId, String type){
        this.mavID = mavId;
        this.name = name;
        this.clubId = clubId;
        this.type = type;
    }

    public Student(String name, String mavID){
        this.name = name;
        this.mavID = mavID;
    }

    public Student(String id, String name, String pw, String email){
        this.mavID = id;
        this.name = name;
        this.password = pw;
        this.email =email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMavID() {
        return mavID;
    }

    public void setMavID(String mavID) {
        this.mavID = mavID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
