package com.example.captainmccann.talkanalyzer.talkDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;


public class TalkDetailFragment extends Fragment implements TalkDetailContract.View {

    public static final String ARGUMENT_TALK_ID = "com.example.captainmccann.talkanalyzer.ARGUMENT_TALK_ID";

    private TalkDetailContract.UserActionsListener userActionsListener;
    private TalkDbHelper database;

    private TextView talkTitle;
    private TextView talkSpeaker;
    private TextView talkDate;
    private TextView talkType;
    private TextView talkTiming;
    private TextView talkScriptureCount;
    private TextView talkIllustrationsCount;
    private TextView talkNotes;

    private int talkId;


    public TalkDetailFragment() {
        // Required empty public constructor
    }

    public static TalkDetailFragment newInstance(int talkId) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TALK_ID, talkId);
        TalkDetailFragment fragment = new TalkDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new TalkDbHelper(getContext());
        userActionsListener = new TalkDetailPresenter(this, database);
    }

    @Override
    public void onResume() {
        super.onResume();
        talkId = getArguments().getInt(ARGUMENT_TALK_ID);
        userActionsListener.openTalk(talkId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_talk_detail, container, false);

        setHasOptionsMenu(true);

        talkTitle = (TextView) v.findViewById(R.id.talk_detail_title);
        talkSpeaker = (TextView) v.findViewById(R.id.talk_detail_speaker);
        talkDate = (TextView) v.findViewById(R.id.talk_detail_date);
        talkType = (TextView) v.findViewById(R.id.talk_detail_type);
        talkTiming = (TextView) v.findViewById(R.id.talk_detail_timing);
        talkScriptureCount = (TextView) v.findViewById(R.id.talk_detail_number_of_scriptures);
        talkIllustrationsCount = (TextView) v.findViewById(R.id.talk_detail_number_of_illustrations);
        talkNotes = (TextView) v.findViewById(R.id.talk_detail_notes);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.talk_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_talk:
                showDeleteTalkDialog();
                return true;

            case R.id.edit_talk:
                userActionsListener.editTalk();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDeleteTalkDialog() {
        //// TODO: 1/23/2017 create delete dialog here
        userActionsListener.deleteTalk(talkId);
    }

    @Override
    public void showTitle(String title) {
        talkTitle.setText(title);
    }

    @Override
    public void showSpeaker(String speaker) {
        talkSpeaker.setText(speaker);
    }

    @Override
    public void showDate(String date) {
        talkDate.setText(date);
    }

    @Override
    public void showType(String type) {
        talkType.setText(type);
    }

    @Override
    public void showOverallTiming(String timing) {
        talkTiming.setText(timing);
    }

    @Override
    public void showNumberOfScriptures(String scriptures) {
        talkScriptureCount.setText(scriptures);
    }

    @Override
    public void showNumberOfIllustrations(String illustrations) {
        talkIllustrationsCount.setText(illustrations);
    }

    @Override
    public void showNotes(String notes) {
        talkNotes.setText(notes);
    }

    @Override
    public void removeDetailView() {
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.closeDB();
    }
}
