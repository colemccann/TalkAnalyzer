package com.example.captainmccann.talkanalyzer.data;

import java.util.ArrayList;

import static java.lang.String.valueOf;

/**
 * Created by CaptainMcCann on 1/18/2017.
 */

public class StatsCruncher implements TalkServiceApi.Cruncher {

    private TalkDbHelper database;

    private static final int ONE_THOUSAND = 1000;
    private static final int SIXTY = 60;
    private static final String ZERO = "0";

    public StatsCruncher(TalkDbHelper database) {
        this.database = database;
    }

    @Override
    public void tallyTalks(boolean publicOnly,
                           final TalkServiceApi.Cruncher.tallyTalksCallback callback) {
        if (publicOnly) {
            database.getPublicTalks(new TalkServiceApi.TalkListServiceCallback() {
                @Override
                public void onTalksLoaded(ArrayList<Talk> talks) {
                    int size = talks.size();
                    callback.onTallyLoaded(size);
                }
            });
        } else {
            database.getAllTalks(new TalkServiceApi.TalkListServiceCallback() {
                @Override
                public void onTalksLoaded(ArrayList<Talk> talks) {
                    int size = talks.size();
                    callback.onTallyLoaded(size);
                }
            });
        }

    }

    @Override
    public void tallyFormattedAverageTiming(boolean publicOnly,
                                            final TalkServiceApi.Cruncher.tallyAverageTimingCallback
                                                    callback) {
        TalkServiceApi.TimingServiceCallback timingServiceCallback =
                new TalkServiceApi.TimingServiceCallback() {
                    @Override
                    public void onTimingsLoaded(long[] timings) {
                        long tempTime;
                        long totalTimeOverall = 0;
                        long averageTime;

                        int minutes;
                        int seconds;
                        for (long i : timings) {
                            tempTime = totalTimeOverall + i;
                            totalTimeOverall = tempTime;
                        }
                        if (timings.length > 0) {
                            averageTime = totalTimeOverall / timings.length;
                            seconds = (int) ((averageTime / ONE_THOUSAND) % SIXTY);
                            minutes = (int) (((averageTime / ONE_THOUSAND) - seconds) / SIXTY);
                        } else {
                            minutes = 0;
                            seconds = 0;
                        }
                        String timing;
                        if (seconds < 10) {
                            String secondsString = ZERO + seconds;
                            timing = minutes + ":" + secondsString;
                        } else {
                            timing = minutes + ":" + seconds;
                        }
                        callback.onTallyLoaded(timing);
                    }
                };
        if (publicOnly) {
            database.getTimingOfPublicTalks(timingServiceCallback);
        } else {
            database.getTimingOfAllTalks(timingServiceCallback);
        }


    }

    @Override
    public void tallyAverageScriptureCount(boolean publicOnly,
                                           final TalkServiceApi.Cruncher.tallyAverageScriptureCountCallback
                                                   callback) {
        TalkServiceApi.CountServiceCallback countServiceCallback =
                new TalkServiceApi.CountServiceCallback() {
            @Override
            public void onCountsLoaded(int[] counts) {
                int tempTally;
                int totalScriptureTally = 0;
                for (int i : counts) {
                    tempTally = totalScriptureTally + i;
                    totalScriptureTally = tempTally;
                }
                double average;
                if (counts.length > 0) {
                    average = Math.round((totalScriptureTally / counts.length) * 10) / 10.0;
                } else average = 0;
                callback.onTallyLoaded(valueOf(average));
            }
        };
        if (publicOnly) {
            database.getScriptureCountOfPublicTalks(countServiceCallback);
        } else {
            database.getScriptureCounts(countServiceCallback);
        }
    }

    @Override
    public void tallyAverageIllustrationCount(boolean publicOnly,
                                              final TalkServiceApi.Cruncher.tallyAverageIllustrationCountCallback
                                              callback) {
        TalkServiceApi.CountServiceCallback countServiceCallback =
                new TalkServiceApi.CountServiceCallback() {
                    @Override
                    public void onCountsLoaded(int[] counts) {
                        int tempTally;
                        int totalIllustrationTally = 0;
                        for (int i : counts) {
                            tempTally = totalIllustrationTally + i;
                            totalIllustrationTally = tempTally;
                        }
                        double average;
                        if (counts.length > 0) {
                            average = Math.round((totalIllustrationTally / counts.length) * 10) / 10.0;
                        } else average = 0;
                        callback.onTallyLoaded(valueOf(average));
                    }
                };
        if (publicOnly) {
            database.getIllustrationCountOfPublicTalks(countServiceCallback);
        } else {
            database.getIllustrationCounts(countServiceCallback);
        }
    }

}
