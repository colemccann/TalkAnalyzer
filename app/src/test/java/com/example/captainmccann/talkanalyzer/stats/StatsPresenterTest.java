package com.example.captainmccann.talkanalyzer.stats;

import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by CaptainMcCann on 1/30/2017.
 */

public class StatsPresenterTest {

    @Mock
    private StatsContract.View statsView;

    @Mock
    private TalkServiceApi.Cruncher cruncher;

    @Captor
    private ArgumentCaptor<TalkServiceApi.Cruncher.tallyTalksCallback>
            tallyTalksCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<TalkServiceApi.Cruncher.tallyAverageTimingCallback>
            tallyAverageTimingCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<TalkServiceApi.Cruncher.tallyAverageScriptureCountCallback>
            tallyAverageScriptureCountCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<TalkServiceApi.Cruncher.tallyAverageIllustrationCountCallback>
            tallyAverageIllustrationCountCallbackArgumentCaptor;

    private StatsPresenter statsPresenter;

    @Before
    public void setupStatsPresenter() {
        MockitoAnnotations.initMocks(this);
        statsPresenter = new StatsPresenter(statsView, cruncher);
    }

    @Test
    public void loadAllStats_showsAllStats() {
        final int TALLY = 1;
        final String CALLBACK = "callback";
        statsPresenter.loadAllStats();

        verify(cruncher).tallyTalks(eq(false), tallyTalksCallbackArgumentCaptor.capture());
        tallyTalksCallbackArgumentCaptor.getValue().onTallyLoaded(TALLY);
        verify(statsView).showNumberOfTalks(TALLY);

        verify(cruncher).tallyFormattedAverageTiming(eq(false), tallyAverageTimingCallbackArgumentCaptor.capture());
        tallyAverageTimingCallbackArgumentCaptor.getValue().onTallyLoaded(CALLBACK);
        verify(statsView).showAverageTiming(CALLBACK);

        verify(cruncher).tallyAverageScriptureCount(eq(false), tallyAverageScriptureCountCallbackArgumentCaptor.capture());
        tallyAverageScriptureCountCallbackArgumentCaptor.getValue().onTallyLoaded(CALLBACK);
        verify(statsView).showAverageScriptureCount(CALLBACK);

        verify(cruncher).tallyAverageIllustrationCount(eq(false), tallyAverageIllustrationCountCallbackArgumentCaptor.capture());
        tallyAverageIllustrationCountCallbackArgumentCaptor.getValue().onTallyLoaded(CALLBACK);
        verify(statsView).showAverageIllustrationCount(CALLBACK);
    }

    @Test
    public void loadPublicStats_showsPublicStats() {
        final int TALLY = 1;
        final String CALLBACK = "callback";
        statsPresenter.loadPublicStatsOnly();

        verify(cruncher).tallyTalks(eq(true), tallyTalksCallbackArgumentCaptor.capture());
        tallyTalksCallbackArgumentCaptor.getValue().onTallyLoaded(TALLY);
        verify(statsView).showNumberOfTalks(TALLY);

        verify(cruncher).tallyFormattedAverageTiming(eq(true), tallyAverageTimingCallbackArgumentCaptor.capture());
        tallyAverageTimingCallbackArgumentCaptor.getValue().onTallyLoaded(CALLBACK);
        verify(statsView).showAverageTiming(CALLBACK);

        verify(cruncher).tallyAverageScriptureCount(eq(true), tallyAverageScriptureCountCallbackArgumentCaptor.capture());
        tallyAverageScriptureCountCallbackArgumentCaptor.getValue().onTallyLoaded(CALLBACK);
        verify(statsView).showAverageScriptureCount(CALLBACK);

        verify(cruncher).tallyAverageIllustrationCount(eq(true), tallyAverageIllustrationCountCallbackArgumentCaptor.capture());
        tallyAverageIllustrationCountCallbackArgumentCaptor.getValue().onTallyLoaded(CALLBACK);
        verify(statsView).showAverageIllustrationCount(CALLBACK);
    }

    @Test
    public void showNumberOfTalks_showsNumberOfTalks() {
        final int NUMBER = 1;
        statsPresenter.showNumberOfTalks(NUMBER);

        verify(statsView).showNumberOfTalks(NUMBER);
    }

    @Test
    public void showAverageTiming_showsAverageTiming() {
        final String NUMBER = "some random string";
        statsPresenter.showTiming(NUMBER);

        verify(statsView).showAverageTiming(NUMBER);
    }

    @Test
    public void showAverageScriptureCount_showsAverageScriptureCount() {
        final String NUMBER = "some random string";
        statsPresenter.showScriptureCount(NUMBER);

        verify(statsView).showAverageScriptureCount(NUMBER);
    }

    @Test
    public void showAverageIllustrationCount_showsAverageIllustrationCount() {
        final String NUMBER = "some random string";
        statsPresenter.showIllustrationCount(NUMBER);

        verify(statsView).showAverageIllustrationCount(NUMBER);
    }
}
