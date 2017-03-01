package com.example.captainmccann.talkanalyzer.stats;


/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public interface StatsContract {

    interface View {

        void showNumberOfTalks(int number);

        void showAverageTiming(String timing);

        void showAverageScriptureCount(String count);

        void showAverageIllustrationCount(String count);

    }

    interface UserActionsListener {

        void loadAllStats();

        void loadPublicStatsOnly();

        void showNumberOfTalks(int numberOfTalks);

        void showTiming(String timing);

        void showScriptureCount(String scriptureCount);

        void showIllustrationCount(String illustrationCount);

    }
}
