package com.example.captainmccann.talkanalyzer.stats;


import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class StatsPresenter implements StatsContract.UserActionsListener {

    private final StatsContract.View statsView;
    private TalkServiceApi.Cruncher cruncher;

    public StatsPresenter(StatsContract.View statsView, TalkServiceApi.Cruncher cruncher) {
        this.statsView = statsView;
        this.cruncher = cruncher;
    }

    private TalkServiceApi.Cruncher.tallyTalksCallback tallyTalksCallback =
            new TalkServiceApi.Cruncher.tallyTalksCallback() {
                        @Override
                        public void onTallyLoaded(int tally) {
                            showNumberOfTalks(tally);
                        }
                    };

    private TalkServiceApi.Cruncher.tallyAverageTimingCallback tallyAverageTimingCallback =
            new TalkServiceApi.Cruncher.tallyAverageTimingCallback() {
                @Override
                public void onTallyLoaded(String formattedTiming) {
                    showTiming(formattedTiming);
                }
            };

    private TalkServiceApi.Cruncher.tallyAverageScriptureCountCallback tallyAverageScriptureCountCallback =
            new TalkServiceApi.Cruncher.tallyAverageScriptureCountCallback() {
                @Override
                public void onTallyLoaded(String count) {
                    showScriptureCount(count);
                }
            };

    private TalkServiceApi.Cruncher.tallyAverageIllustrationCountCallback tallyAverageIllustrationCountCallback =
            new TalkServiceApi.Cruncher.tallyAverageIllustrationCountCallback() {
                @Override
                public void onTallyLoaded(String count) {
                    showIllustrationCount(count);
                }
            };

    @Override
    public void loadAllStats() {
        cruncher.tallyTalks(false, tallyTalksCallback);
        cruncher.tallyFormattedAverageTiming(false, tallyAverageTimingCallback);
        cruncher.tallyAverageScriptureCount(false, tallyAverageScriptureCountCallback);
        cruncher.tallyAverageIllustrationCount(false, tallyAverageIllustrationCountCallback);
    }

    @Override
    public void loadPublicStatsOnly() {
        cruncher.tallyTalks(true, tallyTalksCallback);
        cruncher.tallyFormattedAverageTiming(true, tallyAverageTimingCallback);
        cruncher.tallyAverageScriptureCount(true, tallyAverageScriptureCountCallback);
        cruncher.tallyAverageIllustrationCount(true, tallyAverageIllustrationCountCallback);
    }

    @Override
    public void showNumberOfTalks(int numberOfTalks) {
        statsView.showNumberOfTalks(numberOfTalks);
    }

    @Override
    public void showTiming(String timing) {
        statsView.showAverageTiming(timing);
    }

    @Override
    public void showScriptureCount(String scriptureCount) {
        statsView.showAverageScriptureCount(scriptureCount);
    }

    @Override
    public void showIllustrationCount(String illustrationCount) {
        statsView.showAverageIllustrationCount(illustrationCount);
    }
}
