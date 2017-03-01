package com.example.captainmccann.talkanalyzer.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.captainmccann.talkanalyzer.talks.TalksContract;
import com.example.captainmccann.talkanalyzer.talks.TalksFragment;
import com.example.captainmccann.talkanalyzer.talks.TalksPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class TalkDbHelper extends SQLiteOpenHelper {


        public static class TalkEntries {

            private TalkEntries() {}

            static final String TABLE_NAME = "talks";
            static final String COLUMN_ID = "_id";
            static final String COLUMN_TITLE = "title";
            static final String COLUMN_SPEAKER = "speaker";
            static final String COLUMN_DATE = "date";
            static final String COLUMN_TYPE = "type";
            static final String COLUMN_OVERALL_TIMING = "overall_timing";
            static final String COLUMN_NUM_OF_SCRIPTURES = "num_of_scriptures";
            static final String COLUMN_NUM_OF_ILLUSTRATIONS = "num_of_illustrations";
            static final String COLUMN_NOTES = "notes";

            private static final String SQL_CREATE_ENTRIES =
                    "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                            COLUMN_TITLE + " TEXT, " + COLUMN_SPEAKER + " TEXT, " +
                            COLUMN_DATE + " BIGINT, " + COLUMN_TYPE + " TEXT, " +
                            COLUMN_OVERALL_TIMING + " BIGINT, " + COLUMN_NUM_OF_SCRIPTURES + " INTEGER, " +
                            COLUMN_NUM_OF_ILLUSTRATIONS + " INTEGER, " + COLUMN_NOTES + " TEXT)";

            private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

        }

    // Variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TalkEntries.db";
    private SQLiteDatabase db;

    // Constructor
    public TalkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Methods
    // Create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TalkEntries.SQL_CREATE_ENTRIES);
    }

    // Upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //// TODO: 1/9/2017 determine desired implementation of this
    }

    // Downgrade the database
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    // Open the database
    public TalkDbHelper open() throws SQLException {
        closeDB();
        db = this.getWritableDatabase();
        return this;
    }

    // Close the database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Adding to and updating the database
    // Insert a new database row
    public long insertTalk(String title, String speaker, long date, String type, long overallTiming,
                           int numOfScriptures, int numOfIllustrations, String notes) {

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TalkEntries.COLUMN_TITLE, title);
        values.put(TalkEntries.COLUMN_SPEAKER, speaker);
        values.put(TalkEntries.COLUMN_DATE, date);
        values.put(TalkEntries.COLUMN_TYPE, type);
        values.put(TalkEntries.COLUMN_OVERALL_TIMING, overallTiming);
        values.put(TalkEntries.COLUMN_NUM_OF_SCRIPTURES, numOfScriptures);
        values.put(TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS, numOfIllustrations);
        values.put(TalkEntries.COLUMN_NOTES, notes);

        return db.insertOrThrow(TalkEntries.TABLE_NAME, null, values);
    }

    /*
    **
    * // Updates a talk
    public void updateTalk(int id, String title, String speaker, long date, String type, long overallTiming,
                           int numOfScriptures, int numOfIllustrations, String notes) {

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TalkEntries.COLUMN_TITLE, title);
        values.put(TalkEntries.COLUMN_SPEAKER, speaker);
        values.put(TalkEntries.COLUMN_DATE, date);
        values.put(TalkEntries.COLUMN_TYPE, type);
        values.put(TalkEntries.COLUMN_OVERALL_TIMING, overallTiming);
        values.put(TalkEntries.COLUMN_NUM_OF_SCRIPTURES, numOfScriptures);
        values.put(TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS, numOfIllustrations);
        values.put(TalkEntries.COLUMN_NOTES, notes);

        String selection = TalkEntries.COLUMN_ID + "=" + id;

        db.update(
                TalkEntries.TABLE_NAME,
                values,
                selection,
                null
        );
    }*
    **
    */


    // Deleting a talk
    // Deletes a single talk for the given _id
    public void deleteTalk(int id) {
        String selection = TalkEntries.COLUMN_ID + "=" + id;

        db.delete(TalkEntries.TABLE_NAME, selection, null);
    }

    // Querying the database
    // Returns all Talks in the database with _id
    public void getAllTalks(final TalkServiceApi.TalkListServiceCallback callback) {
        Log.d("getAllTalks", "getAllTalks called");
        db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TalkEntries.TABLE_NAME;
        Cursor cursor =
                db.rawQuery(selectQuery, null);

        ArrayList<Talk> talks = new ArrayList<>();
        try {
        if (cursor.moveToFirst()) {
            do {
                Talk talk = new Talk(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3),
                        cursor.getString(4),
                        cursor.getLong(5),
                        cursor.getInt(6),
                        cursor.getInt(7),
                        cursor.getString(8));
                talks.add(talk);
            } while (cursor.moveToNext());
        }
    } catch (SQLiteException e) {
            Log.d("SQL error", e.getMessage());
            talks.clear();
        }
        cursor.close();
        callback.onTalksLoaded(talks);
    }

    // Returns a list of Talks who's type are Public
    public void getPublicTalks(final TalkServiceApi.TalkListServiceCallback callback) {
        db = this.getReadableDatabase();
        final String TITLE = "title";
        final String SPEAKER = "speaker";
        final long DATE = 0;
        final String TYPE = "type";
        final String NOTES = "notes";
        String[] projection = {
                // The columns from the database that will be used after the query
                TalkEntries.COLUMN_ID,
                TalkEntries.COLUMN_OVERALL_TIMING,
                TalkEntries.COLUMN_NUM_OF_SCRIPTURES,
                TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS,
        };
        // Filter the WHERE results
        String selection = TalkEntries.COLUMN_TYPE + " = ? ";
        String[] selectionArgs = { TypeOfTalk.PUBLIC.getType() };
        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);

        ArrayList<Talk> talks = new ArrayList<>();
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Talk talk = new Talk(cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_ID)),
                                TITLE, SPEAKER, DATE, TYPE,
                                cursor.getLong(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_OVERALL_TIMING)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_SCRIPTURES)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS)),
                                NOTES);
                        talks.add(talk);
                    } while (cursor.moveToNext());
                }
            } catch (SQLiteException e) {
                Log.d("SQL error", e.getMessage());
                talks.clear();
            }
        cursor.close();

        callback.onTalksLoaded(talks);
    }


    // Returns a Talk based on specified id
    public void getTalk(int id, final TalkServiceApi.TalkServiceCallback callback) {
        db = this.getReadableDatabase();
        String selection = TalkEntries.COLUMN_ID + "=" + id;
        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        new String[] {
                                TalkEntries.COLUMN_ID,
                                TalkEntries.COLUMN_TITLE,
                                TalkEntries.COLUMN_SPEAKER,
                                TalkEntries.COLUMN_DATE,
                                TalkEntries.COLUMN_TYPE,
                                TalkEntries.COLUMN_OVERALL_TIMING,
                                TalkEntries.COLUMN_NUM_OF_SCRIPTURES,
                                TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS,
                                TalkEntries.COLUMN_NOTES },
                        selection,
                        null,
                        null,
                        null,
                        null);

        Talk mTalk = null;
        if (cursor != null && cursor.moveToFirst()) {

            mTalk = new Talk(cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_SPEAKER)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_TYPE)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_OVERALL_TIMING)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_SCRIPTURES)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NOTES)));
            cursor.close();
        }
        callback.onTalkLoaded(mTalk);
    }

    // Returns all values in column OVERALL_TIMING
    public void getTimingOfAllTalks(final TalkServiceApi.TimingServiceCallback callback) {
        db = this.getReadableDatabase();
        long[] times;
        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        new String[]{TalkEntries.COLUMN_OVERALL_TIMING},
                        null,
                        null,
                        null,
                        null,
                        null
                );
        times = new long[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            long time = cursor.getLong(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_OVERALL_TIMING));
            times[i] = time;
            i++;
        }
        cursor.close();
        callback.onTimingsLoaded(times);
    }

    //Returns all values in column OVERALL_TIMING where TYPE = Public
    public void getTimingOfPublicTalks(final TalkServiceApi.TimingServiceCallback callback) {
        db = this.getReadableDatabase();
        long[] times;
        String selection = TalkEntries.COLUMN_TYPE + " = ? ";
        String[] selectionArgs = { TypeOfTalk.PUBLIC.getType() };
        String[] projection = { TalkEntries.COLUMN_OVERALL_TIMING };

        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
        times = new long[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            long time = cursor.getLong(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_OVERALL_TIMING));
            times[i] = time;
            i++;
        }
        cursor.close();
        callback.onTimingsLoaded(times);
    }

    //Returns all values in column NUM_OF_SCRIPTURES
    public void getScriptureCounts(final TalkServiceApi.CountServiceCallback callback) {
        db = this.getReadableDatabase();
        int[] counts;
        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        new String[]{TalkEntries.COLUMN_NUM_OF_SCRIPTURES},
                        null,
                        null,
                        null,
                        null,
                        null
                );
        counts = new int[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_SCRIPTURES));
            counts[i] = count;
            i++;
        }
        cursor.close();
        callback.onCountsLoaded(counts);
    }

    public void getScriptureCountOfPublicTalks(TalkServiceApi.CountServiceCallback callback) {
        db = this.getReadableDatabase();
        int[] counts;
        String selection = TalkEntries.COLUMN_TYPE + " = ? ";
        String[] selectionArgs = { TypeOfTalk.PUBLIC.getType() };
        String[] projection = { TalkEntries.COLUMN_NUM_OF_SCRIPTURES };

        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
        counts = new int[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_SCRIPTURES));
            counts[i] = count;
            i++;
        }
        cursor.close();
        callback.onCountsLoaded(counts);
    }

    // Returns all values in NUM_OF_ILLUSTRATIONS
    public void getIllustrationCounts(final TalkServiceApi.CountServiceCallback callback) {
        db = this.getReadableDatabase();
        int [] counts;
        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        new String[]{TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS},
                        null,
                        null,
                        null,
                        null,
                        null
                );
        counts = new int[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS));
            counts[i] = count;
            i++;
        }
        cursor.close();
        callback.onCountsLoaded(counts);
    }

    public void getIllustrationCountOfPublicTalks(final TalkServiceApi.CountServiceCallback callback) {
        db = this.getReadableDatabase();
        int[] counts;
        String selection = TalkEntries.COLUMN_TYPE + " = ? ";
        String[] selectionArgs = { TypeOfTalk.PUBLIC.getType() };
        String[] projection = { TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS };

        Cursor cursor =
                db.query(TalkEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
        counts = new int[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow(TalkEntries.COLUMN_NUM_OF_ILLUSTRATIONS));
            counts[i] = count;
            i++;
        }
        cursor.close();
        callback.onCountsLoaded(counts);
    }

}

















