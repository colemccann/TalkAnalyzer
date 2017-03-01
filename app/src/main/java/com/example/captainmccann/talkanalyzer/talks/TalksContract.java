package com.example.captainmccann.talkanalyzer.talks;

import android.content.Context;
import android.content.Intent;

import com.example.captainmccann.talkanalyzer.data.Talk;

import java.util.List;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public interface TalksContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showTalks(List<Talk> talks);

        void showAddTalk();

        void showTalkDetail(int id);

    }

    interface UserActionsListener {

        void loadTalks();

        void addNewTalk();

        void openTalkDetails(Talk selectedTalk);

        void sendUpdateWidgetBroadcast(Context context);

    }
}
