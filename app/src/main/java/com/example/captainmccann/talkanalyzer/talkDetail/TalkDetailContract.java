package com.example.captainmccann.talkanalyzer.talkDetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public interface TalkDetailContract {

    interface View {

        void showTitle(String title);

        void showSpeaker(String speaker);

        void showDate(String date);

        void showType(String type);

        void showOverallTiming(String timing);

        void showNumberOfScriptures(String scriptures);

        void showNumberOfIllustrations(String illustrations);

        void showNotes(String notes);

        void removeDetailView();

        void showDeleteTalkDialog();

        //void showEditTalk();
    }

    interface UserActionsListener {

        void openTalk(int talkId);

        void showTalk(Talk talk);

        void editTalk();

        void deleteTalk(int talkId);
    }
}
