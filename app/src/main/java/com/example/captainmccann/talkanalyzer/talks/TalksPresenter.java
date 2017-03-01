package com.example.captainmccann.talkanalyzer.talks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;
import com.example.captainmccann.talkanalyzer.widget.WidgetProvider;

import java.util.ArrayList;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class TalksPresenter implements TalksContract.UserActionsListener {

    @NonNull
    private final TalkDbHelper database;
    @NonNull
    private final TalksContract.View talksView;

    public TalksPresenter(@NonNull TalksContract.View talksView,
                          @NonNull TalkDbHelper database) {
        this.talksView = talksView;
        this.database = database;
    }

    @Override
    public void loadTalks() {
        talksView.setProgressIndicator(true);
        database.getAllTalks(new TalkServiceApi.TalkListServiceCallback() {
            @Override
            public void onTalksLoaded(ArrayList<Talk> talks) {
                talksView.showTalks(talks);
            }
        });
        talksView.setProgressIndicator(false);
    }

    @Override
    public void addNewTalk() {
        talksView.showAddTalk();
    }

    @Override
    public void openTalkDetails(Talk selectedTalk) {
        talksView.showTalkDetail(selectedTalk.getId());
    }

    @Override
    public void sendUpdateWidgetBroadcast(Context context) {
            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(context.getApplicationContext())
                    .getAppWidgetIds(new ComponentName(context.getApplicationContext(), WidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
    }
}
