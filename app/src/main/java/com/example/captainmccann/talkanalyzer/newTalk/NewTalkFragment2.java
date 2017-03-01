package com.example.captainmccann.talkanalyzer.newTalk;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.widget.WidgetProvider;

/**
 * Created by CaptainMcCann on 1/9/2017.
 */

public class NewTalkFragment2 extends Fragment implements NewTalkContract.View.SecondFragmentView {

    public static final int TV_SCRIPTURE = 1;
    public static final int TV_ILLUSTRATION = 2;

    public static final int BUTTON_ADD_SCRIPTURE = 3;
    public static final int BUTTON_SUBTRACT_SCRIPTURE = 4;
    public static final int BUTTON_ADD_ILLUSTRATION = 5;
    public static final int BUTTON_SUBTRACT_ILLUSTRATION = 6;

    private static final String TITLE = "com.example.captainmccann.talkanalyzer.TITLE";
    private static final String SPEAKER = "com.example.captainmccann.talkanalyzer.SPEAKER";
    private static final String DATE = "com.example.captainmccann.talkanalyzer.DATE";
    private static final String TYPE = "com.example.captainmccann.talkanalyzer.TYPE";

    private String title;
    private String speaker;
    private long date;
    private String type;
    private int scriptureCount;
    private int illustrationCount;
    private long lastPause;
    private long totalTime;
    private boolean resuming = false;
    private boolean showPause = false;
    private boolean running = false;

    TextView mainTitle;
    Chronometer mainChrono;
    ImageButton mainTimerButton;
    ImageButton mainResetButton;
    TextView mainScriptureCounter;
    Button mainAddScriptureButton;
    Button mainSubtractScriptureButton;
    TextView mainIllustrationCounter;
    Button mainAddIllustrationButton;
    Button mainSubtractIllustrationButton;
    EditText notesEditText;

    private NewTalkContract.UserActionsListener.SecondFragmentUAL userActionsListener;
    private TalkDbHelper database;
    private WidgetProvider widgetProvider;

    public NewTalkFragment2() {}

    public static NewTalkFragment2 newInstance(String title, String speaker,
                                               long date, String type) {
        NewTalkFragment2 fragment2 = new NewTalkFragment2();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(SPEAKER, speaker);
        args.putLong(DATE, date);
        args.putString(TYPE, type);
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        widgetProvider = new WidgetProvider();
        database = new TalkDbHelper(getContext());
        userActionsListener = new NewTalkPresenter.NewTalkPresenter2(this, database);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            speaker = getArguments().getString(SPEAKER);
            date = getArguments().getLong(DATE);
            type = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userActionsListener.openNewTalk2(title, speaker);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_talk_fragment2, container, false);

        setHasOptionsMenu(true);

        mainTitle = (TextView) v.findViewById(R.id.fragment_2_title);
        mainChrono = (Chronometer) v.findViewById(R.id.main_chrono);
        mainTimerButton = (ImageButton) v.findViewById(R.id.main_start_stop_button);
        mainTimerButton.setImageResource(R.drawable.icon_start);
        mainTimerButton.setContentDescription("Start timer");
        mainTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionsListener.startOrResumeOrStop(showPause, resuming);
            }
        });
        mainResetButton = (ImageButton) v.findViewById(R.id.main_reset_button);
        mainResetButton.setContentDescription("Reset timer");
        mainResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionsListener.resetTimer();
            }
        });
        mainScriptureCounter = (TextView) v.findViewById(R.id.main_scripture_counter);
        mainAddScriptureButton = (Button) v.findViewById(R.id.main_scripture_add_button);
        mainAddScriptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionsListener.adjustCount(TV_SCRIPTURE, BUTTON_ADD_SCRIPTURE);
            }
        });
        mainSubtractScriptureButton = (Button) v.findViewById(R.id.main_scripture_subtract_button);
        mainSubtractScriptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionsListener.adjustCount(TV_SCRIPTURE, BUTTON_SUBTRACT_SCRIPTURE);
            }
        });
        mainIllustrationCounter = (TextView) v.findViewById(R.id.main_illustration_counter);
        mainAddIllustrationButton = (Button) v.findViewById(R.id.main_illustration_add_button);
        mainAddIllustrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionsListener.adjustCount(TV_ILLUSTRATION, BUTTON_ADD_ILLUSTRATION);
            }
        });
        mainSubtractIllustrationButton = (Button) v.findViewById(R.id.main_illustration_subtract_button);
        mainSubtractIllustrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionsListener.adjustCount(TV_ILLUSTRATION, BUTTON_SUBTRACT_ILLUSTRATION);
            }
        });
        notesEditText = (EditText) v.findViewById(R.id.new_talk_notes);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.new_talk_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_talk_confirm:
                showTalkList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showTitle(String title, String speaker) {
        mainTitle.setText(String.format(getString(R.string.frag_2_title), title, speaker));
    }

    @Override
    public void startChrono() {
        showPause = true;
        running = true;
        mainTimerButton.setContentDescription("Pause timer");
        userActionsListener.showPause();
        mainChrono.setBase(SystemClock.elapsedRealtime());
        mainChrono.start();
    }

    @Override
    public void stopChrono() {
        showPause = false;
        resuming = true;
        running = false;
        mainTimerButton.setContentDescription("Resume timer");
        userActionsListener.showStart();
        lastPause = SystemClock.elapsedRealtime();
        totalTime = SystemClock.elapsedRealtime() - mainChrono.getBase();
        Log.d("total", "total time = " + totalTime);
        mainChrono.stop();
    }

    @Override
    public void resumeChrono() {
        showPause = true;
        running = true;
        mainTimerButton.setContentDescription("Pause timer");
        userActionsListener.showPause();
        mainChrono.setBase(mainChrono.getBase() + SystemClock.elapsedRealtime() - lastPause);
        mainChrono.start();
    }

    @Override
    public void resetChrono() {
        showPause = false;
        resuming = false;
        running = false;
        totalTime = 0;
        mainTimerButton.setContentDescription("Start timer");
        userActionsListener.showStart();
        mainChrono.setBase(SystemClock.elapsedRealtime());
        mainChrono.stop();
    }

    @Override
    public void displayStopButton() {
        mainTimerButton.setImageResource(R.drawable.icon_pause);
    }

    @Override
    public void displayStartButton() {
        mainTimerButton.setImageResource(R.drawable.icon_start);
    }

    @Override
    public boolean updateCount(int textViewCode, int count) {
        switch (textViewCode) {
            case TV_SCRIPTURE:
                mainScriptureCounter.setText(String.valueOf(count));
                scriptureCount = count;
                return true;

            case TV_ILLUSTRATION:
                mainIllustrationCounter.setText(String.valueOf(count));
                illustrationCount = count;
                return true;

            default:
                return false;
        }
    }

    @Override
    public void showTalkList() {
        if (running) {
            stopChrono();
        }
        String notes = notesEditText.getText().toString();
        Talk talk = new Talk(title, speaker, date, type, totalTime,
                scriptureCount, illustrationCount, notes);
        userActionsListener.saveTalk(talk);

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.closeDB();
    }
}
