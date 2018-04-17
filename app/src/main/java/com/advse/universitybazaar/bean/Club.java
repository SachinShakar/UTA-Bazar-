package com.advse.universitybazaar.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by shrey on 2/26/2018.
 */

public class Club {

    int clubId;
    String clubName;
    String clubDescription;
    String clubOwner;
    //Map<String, String> members;

    public Club() {

    }

//    public Club(int id,String name,String description,String owner, Map<String, String> members) {
//        this.clubId = id;
//        this.clubName = name;
//        this.clubDescription = description;
//        this.clubOwner = owner;
//        this.members = members;
//    }

    public Club(int id,String name,String description,String owner) {
        this.clubId = id;
        this.clubName = name;
        this.clubDescription = description;
        this.clubOwner = owner;
    }


    public int getClubId() {
        return clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public String getClubOwner() {
        return clubOwner;
    }

//    public Map<String, String> getMembers(){
//        return members;
//    }
//
//    public void setMembers(Map<String,String> members) {
//        this.members = members;
//    }
}
