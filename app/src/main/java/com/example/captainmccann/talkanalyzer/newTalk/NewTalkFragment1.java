package com.example.captainmccann.talkanalyzer.newTalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.data.Talk;
import com.example.captainmccann.talkanalyzer.data.TypeOfTalk;

import java.util.ArrayList;
import java.util.Calendar;

public class NewTalkFragment1 extends Fragment implements NewTalkContract.View.FirstFragmentView {

    EditText newTalkTitle;
    EditText newTalkSpeaker;
    private String spinnerSelection;

    private static final int TEMP_ID = 1000;
    private static final long TEMP_LONG = 0;
    private static final int TEMP_INT = 0;
    private static final String TEMP_STRING = "empty";

    private Calendar calendar = Calendar.getInstance();
    private long date = calendar.getTimeInMillis();

    NewTalkContract.UserActionsListener.FirstFragmentUAL userActionsListener;

    public NewTalkFragment1() {
        // Required empty public constructor
    }

    public static NewTalkFragment1 newInstance() {
        return new NewTalkFragment1();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActionsListener = new NewTalkPresenter.NewTalkPresenter1(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_talk_fragment1, container, false);

        setHasOptionsMenu(true);

        newTalkTitle = (EditText) v.findViewById(R.id.new_talk_title);

        newTalkSpeaker = (EditText) v.findViewById(R.id.new_talk_speaker);

        final Spinner newTalkSpinner = (Spinner) v.findViewById(R.id.new_talk_spinner);
        ArrayList<String> spinnerArrayList = TypeOfTalk.getTypes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerArrayList);
        newTalkSpinner.setAdapter(adapter);
        newTalkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerSelection = TypeOfTalk.NONE.getType();
            }
        });

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
                Talk talk = new Talk(TEMP_ID, newTalkTitle.getText().toString(), newTalkSpeaker.getText().toString(),
                        date, spinnerSelection, TEMP_LONG, TEMP_INT, TEMP_INT, TEMP_STRING);
                userActionsListener.checkTalkAndProceed(talk);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showNextFragment(Fragment newInstance) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //// TODO: 1/23/2017 grab info we need for fragment 2: title, speaker, date, and type
        transaction.replace(R.id.new_talk_fragment_frame, newInstance);
        transaction.commit();
    }

    @Override
    public void showNoTitleError() {
        Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoSpeakerError() {
        Toast.makeText(getActivity(), "Please enter the speaker's name", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoTypeSelectedError() {
        Toast.makeText(getActivity(), "Please select talk length", Toast.LENGTH_SHORT).show();
    }
}
