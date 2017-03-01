package com.example.captainmccann.talkanalyzer.newTalk;

import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.data.TypeOfTalk;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by CaptainMcCann on 1/30/2017.
 */

public class NewTalkPresenterTest {

    @Mock
    private NewTalkFragment1 newTalkFragment1;

    @Mock
    private NewTalkFragment2 newTalkFragment2;

    @Mock
    private TalkDbHelper talkDbHelper;

    @Mock
    private NewTalkPresenter.NewTalkPresenter2 mNewTalkPresenter2;

    private NewTalkPresenter.NewTalkPresenter1 newTalkPresenter1;
    private NewTalkPresenter.NewTalkPresenter2 newTalkPresenter2;

    private Talk INVALID_TITLE_TALK = new Talk(1, " ", "x", 0, "x", 0, 0, 0, "x");
    private Talk INVALID_SPEAKER_TALK = new Talk(1, "x", " ", 0, "x", 0, 0, 0, "x");
    private Talk INVALID_TYPE_TALK = new Talk(1, "x", "x", 0, TypeOfTalk.NONE.getType(), 0, 0, 0, "x");
    private Talk TALK = new Talk(1, "x", "x", 0, "x", 0, 0, 0, "x");


    @Before
    public void setupNewTalkPresenter() {
        MockitoAnnotations.initMocks(this);
        newTalkPresenter1 = new NewTalkPresenter.NewTalkPresenter1(newTalkFragment1);
        newTalkPresenter2 = new NewTalkPresenter.NewTalkPresenter2(newTalkFragment2, talkDbHelper);
    }

    @Test
    public void checkTalkShowsError_whenTitleIsInvalid() {
        newTalkPresenter1.checkTalkAndProceed(INVALID_TITLE_TALK);
        verify(newTalkFragment1).showNoTitleError();
    }

    @Test
    public void checkTalkShowsError_whenSpeakerIsInvalid() {
        newTalkPresenter1.checkTalkAndProceed(INVALID_SPEAKER_TALK);
        verify(newTalkFragment1).showNoSpeakerError();
    }

    @Test
    public void checkTalkShowError_whenTypeIsInvalid() {
        newTalkPresenter1.checkTalkAndProceed(INVALID_TYPE_TALK);
        verify(newTalkFragment1).showNoTypeSelectedError();
    }

    @Test
    public void openNewTalk2_showsTitleAndSpeaker() {
        final String TITLE = "title";
        final String SPEAKER = "speaker";
        newTalkPresenter2.openNewTalk2(TITLE, SPEAKER);

        verify(newTalkFragment2).showTitle(TITLE, SPEAKER);
    }

    @Test
    public void startOrResumeOrStop_elicitsCorrectBehavior() {
        newTalkPresenter2.startOrResumeOrStop(true, false);
        verify(newTalkFragment2).stopChrono();

        newTalkPresenter2.startOrResumeOrStop(false, false);
        verify(newTalkFragment2).startChrono();

        newTalkPresenter2.startOrResumeOrStop(false, true);
        verify(newTalkFragment2).resumeChrono();
    }

    @Test
    public void startPauseReset_showsStartPauseReset() {
        newTalkPresenter2.showStart();
        verify(newTalkFragment2).displayStartButton();

        newTalkPresenter2.showPause();
        verify(newTalkFragment2).displayStopButton();

        newTalkPresenter2.resetTimer();
        verify(newTalkFragment2).resetChrono();
    }

    @Test
    public void saveTalk_insertsIntoDatabase() {
        newTalkPresenter2.saveTalk(TALK);

        verify(talkDbHelper).insertTalk(TALK.getTitle(),
                TALK.getSpeaker(),
                TALK.getDate(),
                TALK.getType(),
                TALK.getTiming(),
                TALK.getNumOfScriptures(),
                TALK.getNumOfIllustrations(),
                TALK.getNotes());
    }

    @Test
    public void adjustScriptureCount_setsScriptureCount() {
        int count = 0;
        newTalkPresenter2.adjustCount(NewTalkFragment2.TV_SCRIPTURE, NewTalkFragment2.BUTTON_ADD_SCRIPTURE);
        verify(newTalkFragment2).updateCount(NewTalkFragment2.TV_SCRIPTURE, ++count);

        newTalkPresenter2.adjustCount(NewTalkFragment2.TV_SCRIPTURE, NewTalkFragment2.BUTTON_SUBTRACT_SCRIPTURE);
        verify(newTalkFragment2).updateCount(NewTalkFragment2.TV_SCRIPTURE, --count);

        newTalkPresenter2.adjustCount(NewTalkFragment2.TV_ILLUSTRATION, NewTalkFragment2.BUTTON_ADD_ILLUSTRATION);
        verify(newTalkFragment2).updateCount(NewTalkFragment2.TV_ILLUSTRATION, ++count);

        newTalkPresenter2.adjustCount(NewTalkFragment2.TV_ILLUSTRATION, NewTalkFragment2.BUTTON_SUBTRACT_ILLUSTRATION);
        verify(newTalkFragment2).updateCount(NewTalkFragment2.TV_ILLUSTRATION, --count);

    }

}
