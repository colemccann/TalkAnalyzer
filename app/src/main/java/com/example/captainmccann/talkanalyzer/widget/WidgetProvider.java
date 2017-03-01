package com.example.captainmccann.talkanalyzer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.data.StatsCruncher;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;
import com.example.captainmccann.talkanalyzer.newTalk.NewTalkActivity;
import com.example.captainmccann.talkanalyzer.talks.TalksActvity;

import static java.lang.String.valueOf;

/**
 * Created by CaptainMcCann on 2/20/2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    private final String WIDTH = "com.example.captainmccann.talkanalyzer.WIDTH";
    private final String PREF_NAME = "com.example.captainmccann.talkanalyzer.PREF_NAME";

    private String tallyString;
    private int minWidth;
    private SharedPreferences preferences;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        TalkServiceApi.Cruncher cruncher = new StatsCruncher(new TalkDbHelper(context));
        cruncher.tallyTalks(false, tallyTalksCallback);
        String talksAnalyzed = context.getString(R.string.talks_analyzed) + tallyString;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        minWidth = preferences.getInt(WIDTH, 250);

        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, NewTalkActivity.class);
            PendingIntent pendingIntentNewTalk = PendingIntent.getActivity(context, 0, intent, 0);

            Intent intent2 = new Intent(context, TalksActvity.class);
            PendingIntent pendingIntentHome = PendingIntent.getActivity(context, 0, intent2, 0);

            RemoteViews views = getRemoteViews(context, minWidth);
            views.setOnClickPendingIntent(R.id.widget_button, pendingIntentNewTalk);
            views.setOnClickPendingIntent(R.id.widget_home_button, pendingIntentHome);
            views.setTextViewText(R.id.widget_textview, talksAnalyzed);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(WIDTH, minWidth);
        editor.apply();

        appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context, minWidth));
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private RemoteViews getRemoteViews(Context context, int minWidth) {
        TalkServiceApi.Cruncher cruncher = new StatsCruncher(new TalkDbHelper(context));
        cruncher.tallyTalks(false, tallyTalksCallback);
        String talksAnalyzed = context.getString(R.string.talks_analyzed) + tallyString;

        Intent intent = new Intent(context, NewTalkActivity.class);
        PendingIntent pendingIntentNewTalk = PendingIntent.getActivity(context, 0, intent, 0);

        Intent intent2 = new Intent(context, TalksActvity.class);
        PendingIntent pendingIntentHome = PendingIntent.getActivity(context, 0, intent2, 0);

        int columns = getCellsForSize(minWidth);
        switch (columns) {
            case 4: RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout_default);
                views.setOnClickPendingIntent(R.id.widget_button, pendingIntentNewTalk);
                views.setTextViewText(R.id.widget_textview, talksAnalyzed);
                return views;
            case 5: RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.widget_layout_5cells);
                views2.setOnClickPendingIntent(R.id.widget_button, pendingIntentNewTalk);
                views2.setOnClickPendingIntent(R.id.widget_home_button, pendingIntentHome);
                views2.setTextViewText(R.id.widget_textview, talksAnalyzed);
                return views2;
            default: RemoteViews views3 = new RemoteViews(context.getPackageName(), R.layout.widget_layout_default);
                views3.setOnClickPendingIntent(R.id.widget_button, pendingIntentNewTalk);
                views3.setTextViewText(R.id.widget_textview, talksAnalyzed);
                return views3;
        }
    }

    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size) {
            ++n;
        }
        return n - 1;
    }

    private TalkServiceApi.Cruncher.tallyTalksCallback tallyTalksCallback =
            new TalkServiceApi.Cruncher.tallyTalksCallback() {
                @Override
                public void onTallyLoaded(int tally) {
                    tallyString = valueOf(tally);
                }
            };
}
