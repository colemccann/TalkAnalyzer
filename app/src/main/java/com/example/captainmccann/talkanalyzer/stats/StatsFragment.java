package com.example.captainmccann.talkanalyzer.stats;

import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.data.StatsCruncher;
import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.data.TalkServiceApi;
import com.example.captainmccann.talkanalyzer.data.TypeOfTalk;

import java.util.ArrayList;

public class StatsFragment extends Fragment implements StatsContract.View {

    private StatsContract.UserActionsListener userActionsListener;
    private TalkDbHelper database;

    Spinner statsSpinner;
    TextView statsTotalTalks;
    TextView statsAvgTiming;
    TextView statsAvgScripCount;
    TextView statsAvgIllusCount;

    public StatsFragment() {}

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new TalkDbHelper(getContext());
        userActionsListener = new StatsPresenter(this, new StatsCruncher(database));
    }

    @Override
    public void onResume() {
        super.onResume();
        //// TODO: 1/24/2017 fix this so that it remembers your choice of either public or other until the fragment is destroyed
        //// TODO: 1/24/2017 Add a progress indicator
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        statsSpinner = (Spinner) v.findViewById(R.id.stats_spinner);
        final ArrayList<String> list = new ArrayList<>();
        list.add(TypeOfTalk.PUBLIC.getType());
        list.add(TypeOfTalk.ALL.getType());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        statsSpinner.setAdapter(adapter);
        statsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals(TypeOfTalk.PUBLIC.getType())) {
                    userActionsListener.loadPublicStatsOnly();
                } else if (item.equals(TypeOfTalk.ALL.getType())) {
                    userActionsListener.loadAllStats();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        statsTotalTalks = (TextView) v.findViewById(R.id.stats_total_talks);
        statsAvgTiming = (TextView) v.findViewById(R.id.stats_avg_timing);
        statsAvgScripCount = (TextView) v.findViewById(R.id.stats_avg_scrip_count);
        statsAvgIllusCount = (TextView) v.findViewById(R.id.stats_avg_illus_count);

        return v;
    }

    @Override
    public void showNumberOfTalks(int number) {
        statsTotalTalks.setText(String.valueOf(number));
    }

    @Override
    public void showAverageTiming(String timing) {
        statsAvgTiming.setText(timing);
    }

    @Override
    public void showAverageScriptureCount(String count) {
        statsAvgScripCount.setText(String.valueOf(count));
    }

    @Override
    public void showAverageIllustrationCount(String count) {
        statsAvgIllusCount.setText(String.valueOf(count));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.closeDB();
    }
}
