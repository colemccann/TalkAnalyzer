package com.example.captainmccann.talkanalyzer.talks;



import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;

import static org.mockito.Mockito.verify;

/**
 * Created by CaptainMcCann on 1/27/2017.
 */


public class talksPresenterTest {



    @Mock
    private TalkDbHelper talkDbHelper;

    @Mock
    private TalksContract.View talksView;

    @Captor
    private ArgumentCaptor<TalkServiceApi.TalkListServiceCallback> talkServiceCallbackArgumentCaptor;

    private TalksPresenter talksPresenter;
    private Talk talk = new Talk(1, "x", "x", 0, "x", 0, 0, 0, "x");
    private ArrayList<Talk> talks = new ArrayList<>();


    @Before
    public void setupTalksPresenter() {
        MockitoAnnotations.initMocks(this);
        talksPresenter = new TalksPresenter(talksView, talkDbHelper);
    }

    @Test
    public void loadTalks_showsTalks() {
        talksPresenter.loadTalks();

        verify(talkDbHelper).getAllTalks(talkServiceCallbackArgumentCaptor.capture());

        talkServiceCallbackArgumentCaptor.getValue().onTalksLoaded(talks);

        verify(talksView).showTalks(talks);
        verify(talksView).setProgressIndicator(true);
    }

    @Test
    public void clickOnFab_showsAddTalk() {
        talksPresenter.addNewTalk();

        verify(talksView).showAddTalk();
    }

    @Test
    public void clickOnTalk_showsTalkDetails() {
        talksPresenter.openTalkDetails(talk);

        verify(talksView).showTalkDetail(talk.getId());
    }

}
