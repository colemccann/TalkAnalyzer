package com.example.captainmccann.talkanalyzer.data;


import java.util.ArrayList;

/**
 * Created by CaptainMcCann on 1/18/2017.
 */

public interface TalkServiceApi {

    interface Cruncher {

        void tallyTalks(boolean publicOnly, tallyTalksCallback callback);
        interface tallyTalksCallback {
            void onTallyLoaded(int tally);
        }

        void tallyFormattedAverageTiming(boolean publicOnly, tallyAverageTimingCallback callback);
        interface tallyAverageTimingCallback {
            void onTallyLoaded(String formattedTiming);
        }

        void tallyAverageScriptureCount(boolean publicOnly, tallyAverageScriptureCountCallback callback);
        interface tallyAverageScriptureCountCallback {
            void onTallyLoaded(String count);
        }

        void tallyAverageIllustrationCount(boolean publicOnly, tallyAverageIllustrationCountCallback callback);
        interface tallyAverageIllustrationCountCallback {
            void onTallyLoaded(String count);
        }

    }

    interface TalkServiceCallback {

        void onTalkLoaded(Talk talk);
    }

    interface TalkListServiceCallback {

        void onTalksLoaded(ArrayList<Talk> talks);
    }

    interface TimingServiceCallback {

        void onTimingsLoaded(long[] timings);
    }

    interface CountServiceCallback {

        void onCountsLoaded(int[] counts);
    }

}
