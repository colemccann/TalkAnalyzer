package com.example.captainmccann.talkanalyzer.data;

import java.util.ArrayList;

/**
 * Created by CaptainMcCann on 1/21/2017.
 */

public enum TypeOfTalk {

    NONE("Select Talk Type"), PUBLIC("Public"), OTHER("Other"), ALL("All");

    private String type;

    // Public constructor
    TypeOfTalk(String type) {this.type = type;}

    // Methods
    public String getType() { return type;}

    public static ArrayList<String> getTypes() {
        ArrayList<String> list = new ArrayList<>();
        list.add(NONE.getType());
        list.add(PUBLIC.getType());
        list.add(OTHER.getType());
        return list;
    }

}
