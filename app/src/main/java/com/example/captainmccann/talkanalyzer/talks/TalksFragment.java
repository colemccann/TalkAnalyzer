package com.example.captainmccann.talkanalyzer.talks;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captainmccann.talkanalyzer.R;
import com.example.captainmccann.talkanalyzer.data.Talk;

import com.example.captainmccann.talkanalyzer.data.TalkDbHelper;
import com.example.captainmccann.talkanalyzer.newTalk.NewTalkActivity;
import com.example.captainmccann.talkanalyzer.talkDetail.TalkDetailActivity;
import com.example.captainmccann.talkanalyzer.widget.WidgetProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaptainMcCann on 1/11/2017.
 */

public class TalksFragment extends Fragment implements TalksContract.View {

    private TalksContract.UserActionsListener userActionsListener;
    private TalkListAdapter talkListAdapter;
    private TalkDbHelper database;

    public static final int REQUEST_ADD_TALK = 1;

    public TalksFragment(){}

    public static Fragment newInstance() {
        return new TalksFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        talkListAdapter = new TalkListAdapter(new ArrayList<Talk>(), itemListener);
        database = new TalkDbHelper(getContext());
        userActionsListener = new TalksPresenter(this, database);
    }

    @Override
    public void onResume(){
        super.onResume();
        userActionsListener.loadTalks();
        userActionsListener.sendUpdateWidgetBroadcast(getContext());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_talks, container, false);

        final RecyclerView theList = (RecyclerView) v.findViewById(R.id.talks_recyclerView);
        theList.setAdapter(talkListAdapter);
        theList.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.talks_fab);
        fab.setContentDescription("");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userActionsListener.addNewTalk();
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.talks_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userActionsListener.loadTalks();
            }
        });

        return v;
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)
                getView().findViewById(R.id.talks_refresh_layout);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTalks(List<Talk> talks) {
        talkListAdapter.replaceData(talks);
    }

    @Override
    public void showAddTalk() {
        Intent intent = new Intent(getActivity(), NewTalkActivity.class);
        startActivityForResult(intent, REQUEST_ADD_TALK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ADD_TALK == requestCode && Activity.RESULT_OK == resultCode) {
            Toast.makeText(getContext(), "Talk Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showTalkDetail(int id) {
        Intent intent = new Intent(getActivity(), TalkDetailActivity.class);
        intent.putExtra(TalkDetailActivity.TALK_DETAIL_ID, id);
        startActivity(intent);
    }

    TalkItemListener itemListener = new TalkItemListener() {
        @Override
        public void onTalkClick(Talk clickedTalk) {
            userActionsListener.openTalkDetails(clickedTalk);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.closeDB();
    }

    private static class TalkListAdapter extends RecyclerView.Adapter<TalkListAdapter.ViewHolder> {

        private List<Talk> talks;
        private TalkItemListener talkItemListener;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView title;
            TextView speakerName;
            TextView date;

            private TalkItemListener talkItemListener;

            ViewHolder(View itemView, TalkItemListener listener) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.item_talk_title);
                speakerName = (TextView) itemView.findViewById(R.id.item_speaker_name);
                date = (TextView) itemView.findViewById(R.id.item_talk_date);

                talkItemListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Talk talk = getItem(position);
                talkItemListener.onTalkClick(talk);
            }
        }

        TalkListAdapter(List<Talk> talks, TalkItemListener listener) {
            setTalks(talks);
            talkItemListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View talkView = inflater.inflate(R.layout.talk_list_item, parent, false);

            return new ViewHolder(talkView, talkItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Talk talk = talks.get(position);

            holder.title.setText(talk.getTitle());
            holder.speakerName.setText(talk.getSpeaker());
            holder.date.setText(talk.getReadableDate());
        }

        @Override
        public int getItemCount() {
            return talks.size();
        }

        void replaceData(List<Talk> talks) {
            setTalks(talks);
            notifyDataSetChanged();
        }

        private void setTalks(List<Talk> talks) {
            this.talks = talks;
        }

        Talk getItem(int position) {
            return talks.get(position);
        }
    }

    public interface TalkItemListener {

        void onTalkClick(Talk clickedTalk);
    }
}
