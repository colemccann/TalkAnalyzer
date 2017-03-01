package com.example.captainmccann.talkanalyzer.newTalk;



import android.support.v4.app.Fragment;

import com.example.captainmccann.talkanalyzer.data.Talk;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public interface NewTalkContract {

    interface View /* For showing, hiding, manipulating UI elements */ {

        interface FirstFragmentView {

            void showNoTitleError();

            void showNoSpeakerError();

            void showNoTypeSelectedError();

            void showNextFragment(Fragment newInstance);

        }

        interface SecondFragmentView {

            void showTitle(String title, String speaker);

            void startChrono();

            void stopChrono();

            void resumeChrono();

            void resetChrono();

            void displayStopButton();

            void displayStartButton();

            boolean updateCount(int textViewCode, int count);

            void showTalkList();

        }
    }

    interface UserActionsListener /* For reacting to user actions! */ {

        interface FirstFragmentUAL {

            void checkTalkAndProceed(Talk talk);

            void checkTitle(Talk talk);

            void checkSpeaker(Talk talk);

            void checkType(Talk talk);

            void nextFragment(String title, String speaker,long date, String type);
        }

        interface SecondFragmentUAL {

            void openNewTalk2(String title, String speaker);

            void startOrResumeOrStop(boolean pausing, boolean resuming);

            void showStart();

            void showPause();

            void resetTimer();

            boolean adjustCount(int textViewCode, int buttonCode);

            void saveTalk(Talk talk);
        }


    }
}
