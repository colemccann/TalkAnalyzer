package com.example.captainmccann.talkanalyzer.talkDetail;

import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.IDN;
import java.util.ArrayList;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by CaptainMcCann on 1/30/2017.
 */

public class TalkDetailPresenterTest {

    @Mock
    private TalkDetailContract.View talkDetailView;

    @Mock
    private TalkDbHelper talkDbHelper;

    @Captor
    private ArgumentCaptor<TalkServiceApi.TalkServiceCallback> talkServiceCallbackArgumentCaptor;

    private TalkDetailPresenter talkDetailPresenter;

    @Before
    public void setupTalkDetailPresenter() {
        MockitoAnnotations.initMocks(this);
        talkDetailPresenter = new TalkDetailPresenter(talkDetailView, talkDbHelper);
    }

    @Test
    public void openTalk_displaysTalk() {
        Talk TALK = new Talk(1, "x", "x", 0, "x", 0, 0, 0, "x");
        talkDetailPresenter.openTalk(TALK.getId());

        verify(talkDbHelper).getTalk(eq(TALK.getId()), talkServiceCallbackArgumentCaptor.capture());

        talkServiceCallbackArgumentCaptor.getValue().onTalkLoaded(TALK);

        verify(talkDetailView).showTitle(TALK.getTitle());
        verify(talkDetailView).showSpeaker(TALK.getSpeaker());
        verify(talkDetailView).showDate(TALK.getReadableDate());
        verify(talkDetailView).showType(TALK.getType());
        verify(talkDetailView).showOverallTiming(TALK.getFormattedTiming());
        verify(talkDetailView).showNumberOfScriptures(TALK.getStringNumOfScriptures());
        verify(talkDetailView).showNumberOfIllustrations(TALK.getStringNumOfIllustrations());
        verify(talkDetailView).showNotes(TALK.getNotes());
    }

    @Test
    public void deleteTalk_deletesTalkFromDatabase() {
        final int ID = 1;
        talkDetailPresenter.deleteTalk(ID);

        verify(talkDbHelper).deleteTalk(ID);
        verify(talkDetailView).removeDetailView();
    }
}
