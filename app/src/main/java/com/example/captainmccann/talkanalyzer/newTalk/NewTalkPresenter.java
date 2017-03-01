package com.example.captainmccann.talkanalyzer.newTalk;

import android.util.Log;

import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class NewTalkPresenter {

    public static class NewTalkPresenter1 implements NewTalkContract.UserActionsListener.FirstFragmentUAL {

        private final NewTalkContract.View.FirstFragmentView firstFragmentView;

        public NewTalkPresenter1(NewTalkContract.View.FirstFragmentView firstFragmentView) {
            this.firstFragmentView = firstFragmentView;
        }

        @Override
        public void checkTalkAndProceed(Talk talk) {
            checkTitle(talk);
        }

        @Override
        public void checkTitle(Talk talk) {
            if (talk.hasValidTitle()) {
                checkSpeaker(talk);
            } else {
                firstFragmentView.showNoTitleError();
            }
        }

        @Override
        public void checkSpeaker(Talk talk) {
            if (talk.hasValidSpeaker()) {
                checkType(talk);
            } else {
                firstFragmentView.showNoSpeakerError();
            }
        }

        @Override
        public void checkType(Talk talk) {
            if (talk.hasValidType()) {
                nextFragment(talk.getTitle(), talk.getSpeaker(), talk.getDate(), talk.getType());
            } else {
                firstFragmentView.showNoTypeSelectedError();
            }
        }

        @Override
        public void nextFragment(String title, String speaker, long date, String type) {
            firstFragmentView.showNextFragment(NewTalkFragment2.newInstance(title, speaker,
                                                        date, type));
        }
    }

    public static class NewTalkPresenter2 implements NewTalkContract.UserActionsListener.SecondFragmentUAL {

        private TalkDbHelper database;
        private final NewTalkContract.View.SecondFragmentView secondFragmentView;

        private int count;
        private int scriptureCount;
        private int illustrationCount;


        public NewTalkPresenter2(NewTalkContract.View.SecondFragmentView secondFragmentView,
                                 TalkDbHelper database) {
            this.database = database;
            this.secondFragmentView = secondFragmentView;
        }

        @Override
        public void openNewTalk2(String title, String speaker) {
            secondFragmentView.showTitle(title, speaker);
        }

        @Override
        public void startOrResumeOrStop(boolean pausing, boolean resuming) {
            if (pausing) {
                secondFragmentView.stopChrono();
            } else {
                if (resuming) {
                    secondFragmentView.resumeChrono();
                } else {
                    secondFragmentView.startChrono();
                }
            }
        }

        @Override
        public void showStart() {
            secondFragmentView.displayStartButton();
        }

        @Override
        public void showPause() {
            secondFragmentView.displayStopButton();
        }

        @Override
        public void resetTimer() {
            secondFragmentView.resetChrono();
        }

        @Override
        public void saveTalk(Talk talk) {
            database.insertTalk(talk.getTitle(), talk.getSpeaker(), talk.getDate(), talk.getType(),
                    talk.getTiming(), talk.getNumOfScriptures(), talk.getNumOfIllustrations(),
                    talk.getNotes());
        }

        @Override
        public boolean adjustCount(int textViewCode, int buttonCode) {
            switch (buttonCode) {
                case NewTalkFragment2.BUTTON_ADD_SCRIPTURE:
                    ++scriptureCount;
                    count = scriptureCount;
                    break;
                case NewTalkFragment2.BUTTON_SUBTRACT_SCRIPTURE:
                    if (scriptureCount <= 0) {
                        scriptureCount = 0;
                    } else {
                        --scriptureCount;
                    }
                    count = scriptureCount;
                    break;
                case NewTalkFragment2.BUTTON_ADD_ILLUSTRATION:
                    ++illustrationCount;
                    count = illustrationCount;
                    break;
                case NewTalkFragment2.BUTTON_SUBTRACT_ILLUSTRATION:
                    if (illustrationCount <= 0) {
                        illustrationCount = 0;
                    } else {
                        --illustrationCount;
                    }
                    count = illustrationCount;
                    break;
            }
            return secondFragmentView.updateCount(textViewCode, count);
        }
    }
}
