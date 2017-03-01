package com.example.captainmccann.talkanalyzer.talkDetail;

import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class TalkDetailPresenter implements TalkDetailContract.UserActionsListener {

    private TalkDbHelper database;
    private final TalkDetailContract.View talkDetailView;

    public TalkDetailPresenter(TalkDetailContract.View talkDetailView, TalkDbHelper database) {
        this.talkDetailView = talkDetailView;
        this.database = database;
    }

    @Override
    public void openTalk(int talkId) {
        database.getTalk(talkId, new TalkServiceApi.TalkServiceCallback() {
            @Override
            public void onTalkLoaded(Talk talk) {
                showTalk(talk);
            }
        });
    }

    @Override
    public void showTalk(Talk talk) {
        String title = talk.getTitle();
        String speaker = talk.getSpeaker();
        String date = talk.getReadableDate();
        String type = talk.getType();
        String timing = talk.getFormattedTiming();
        String scriptureCount = talk.getStringNumOfScriptures();
        String illustrationCount = talk.getStringNumOfIllustrations();
        String notes = talk.getNotes();

        talkDetailView.showTitle(title);
        talkDetailView.showSpeaker(speaker);
        talkDetailView.showDate(date);
        talkDetailView.showType(type);
        talkDetailView.showOverallTiming(timing);
        talkDetailView.showNumberOfScriptures(scriptureCount);
        talkDetailView.showNumberOfIllustrations(illustrationCount);
        talkDetailView.showNotes(notes);
    }


    @Override
    public void editTalk() {

    }

    @Override
    public void deleteTalk(int talkId) {
        database.deleteTalk(talkId);
        talkDetailView.removeDetailView();
    }
}
