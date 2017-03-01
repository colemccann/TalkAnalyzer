package com.example.captainmccann.talkanalyzer.data;

import java.util.Calendar;
import java.util.Date;

import static java.lang.String.valueOf;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class Talk {

    private int id;
    private String title;
    private String speaker;
    private long date;
    private String type;
    private long overallTiming;
    private int numOfScriptures;
    private int numOfIllustrations;
    private String notes;

    private static final int ONE_THOUSAND = 1000;
    private static final int SIXTY = 60;
    private static final String ZERO = "0";


    public Talk(int id, String title, String speaker, long date, String type, long overallTiming, int numOfScriptures,
                int numOfIllustrations, String notes) {
        this.id = id;
        this.title = title;
        this.speaker = speaker;
        this.date = date;
        this.type = type;
        this.overallTiming = overallTiming;
        this.numOfScriptures = numOfScriptures;
        this.numOfIllustrations = numOfIllustrations;
        this.notes = notes;

    }

    public Talk(String title, String speaker, long date, String type, long overallTiming, int numOfScriptures,
                int numOfIllustrations, String notes) {
        this.title = title;
        this.speaker = speaker;
        this.date = date;
        this.type = type;
        this.overallTiming = overallTiming;
        this.numOfScriptures = numOfScriptures;
        this.numOfIllustrations = numOfIllustrations;
        this.notes = notes;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public long getDate() {
        return date;
    }

    public String getReadableDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(this.date);
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        return (month + 1) + "/" + day + "/" + year;
    }

    public String getType() {
        return type;
    }

    public long getTiming() {
        return overallTiming;
    }

    public String getFormattedTiming() {
        int minutes = (int) ((overallTiming / (ONE_THOUSAND*SIXTY)) % SIXTY);
        int seconds = (int) ((overallTiming / ONE_THOUSAND) % SIXTY);
        if (seconds < 10) {
            String secondsString = ZERO + seconds;
            return minutes + ":" + secondsString;
        } else {
            return minutes + ":" + seconds;
        }
    }

    public int getNumOfScriptures() {
        return numOfScriptures;
    }

    public String getStringNumOfScriptures() {
        return valueOf(numOfScriptures);
    }

    public int getNumOfIllustrations() {
        return numOfIllustrations;
    }

    public String getStringNumOfIllustrations() {
        return valueOf(numOfIllustrations);
    }

    public String getNotes() {
        return notes;
    }

    public boolean hasValidTitle() {
            return title != null && (title.trim().length() > 0);
    }

    public boolean hasValidSpeaker() {
        return speaker != null && (speaker.trim().length() > 0);
    }

    public boolean hasValidType() {
        return type != null && !type.equals(TypeOfTalk.NONE.getType());
    }

}
