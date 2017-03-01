package com.example.captainmccann.talkanalyzer.data;

import com.example.captainmccann.talkanalyzer.stats.StatsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

/**
 * Created by CaptainMcCann on 1/30/2017.
 */

public class CruncherTest {

    @Mock
    private TalkDbHelper talkDbHelper;

    @Mock
    private StatsPresenter statsPresenter;

    @Mock
    private TalkServiceApi.Cruncher.tallyTalksCallback tallyTalksCallback;
    @Captor
    private ArgumentCaptor<TalkServiceApi.TalkListServiceCallback> talkListServiceCallback;

    @Mock
    private TalkServiceApi.Cruncher.tallyAverageTimingCallback tallyAverageTimingCallback;
    @Captor
    private ArgumentCaptor<TalkServiceApi.TimingServiceCallback> timingServiceCallback;

    @Mock
    private  TalkServiceApi.Cruncher.tallyAverageScriptureCountCallback tallyAverageScriptureCountCallback;
    @Captor
    private ArgumentCaptor<TalkServiceApi.CountServiceCallback> countServiceCallback;

    @Mock
    private TalkServiceApi.Cruncher.tallyAverageIllustrationCountCallback tallyAverageIllustrationCountCallback;

    private StatsCruncher statsCruncher;

    @Before
    public void setupStatsCruncher() {
        MockitoAnnotations.initMocks(this);
        statsCruncher = new StatsCruncher(talkDbHelper);
    }

    @Test
    public void tallyTalks_callsBackToPresenter() {
        ArrayList<Talk> talks = new ArrayList<>();
        final int SIZE = 0;
        statsCruncher.tallyTalks(false, tallyTalksCallback);

        verify(talkDbHelper).getAllTalks(talkListServiceCallback.capture());
        talkListServiceCallback.getValue().onTalksLoaded(talks);

        verify(tallyTalksCallback).onTallyLoaded(SIZE);
    }

    @Test
    public void tallyAverageTiming_callsBackToPresenter() {
        final long[] timings = {1};
        final String TIMING = "0:00";
        statsCruncher.tallyFormattedAverageTiming(false, tallyAverageTimingCallback);

        verify(talkDbHelper).getTimingOfAllTalks(timingServiceCallback.capture());
        timingServiceCallback.getValue().onTimingsLoaded(timings);

        verify(tallyAverageTimingCallback).onTallyLoaded(TIMING);
    }

    @Test
    public void tallyAverageScriptureCount_callsBackToPresenter() {
        final int[] TALLY = {1};
        final String AVERAGE = "1.0";
        statsCruncher.tallyAverageScriptureCount(false, tallyAverageScriptureCountCallback);

        verify(talkDbHelper).getScriptureCounts(countServiceCallback.capture());
        countServiceCallback.getValue().onCountsLoaded(TALLY);

        verify(tallyAverageScriptureCountCallback).onTallyLoaded(AVERAGE);
    }

    @Test
    public void tallyAverageIllustrationCount_callsBackToPresenter() {
        final int[] TALLY = {1, 3, 7, 1};
        final String AVERAGE = "3.0";
        statsCruncher.tallyAverageIllustrationCount(false, tallyAverageIllustrationCountCallback);

        verify(talkDbHelper).getIllustrationCounts(countServiceCallback.capture());
        countServiceCallback.getValue().onCountsLoaded(TALLY);

        verify(tallyAverageIllustrationCountCallback).onTallyLoaded(AVERAGE);
    }
}
