package com.burakgalisa.android.remembertodothem;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChoreListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mChoreRecyclerView;
    private ChoreAdapter mAdapter;
    private boolean mSubtitleVisible;
    private LinearLayout mLinearLayout;
    private Button mNewChoreButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_chore_list, container, false);

        mChoreRecyclerView = (RecyclerView) view.findViewById(R.id.chore_recycler_view);
        mChoreRecyclerView.setHasFixedSize(true);
        mChoreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_chore_list_first_view);

        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        }
        updateUI();

        if (ChoreArchive.getChoreArchive(getActivity()).getChores().size() == 0){
            mChoreRecyclerView.setVisibility(View.GONE);
            mNewChoreButton = (Button) view.findViewById(R.id.fragment_chore_list_first_chore);
            mNewChoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chore chore = new Chore();
                    ChoreArchive.getChoreArchive(getActivity()).addChore(chore);
                    Intent intent = ChorePagerActivity.newIntent(getActivity(), chore.getUUID());
                    startActivity(intent);
                }
            });

        }else{
            mLinearLayout.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_chore_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    private void updateUI(){
        ChoreArchive choreArchive = ChoreArchive.getChoreArchive(getActivity());
        List<Chore> chores = choreArchive.getChores();

        if (mAdapter == null){
            mAdapter = new ChoreAdapter(chores);
            mChoreRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setChores(chores);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.notifyDataSetChanged();
        updateSubtitle();
    }

    private class ChoreHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mDoneCheckBox;

        private Chore mChore;

        public ChoreHolder(View itemView){
            super(itemView);

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_chore_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_chore_date_text_view);
            mDoneCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_chore_done_check_box);
            mDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mChore.setDone(mDoneCheckBox.isChecked());
                }
            });

        }


        public void bindChore(Chore chore){
            mChore = chore;
            mTitleTextView.setText(mChore.getTitle());
            mDateTextView.setText(mChore.getDate().toString());
            mDoneCheckBox.setChecked(mChore.isDone());
        }

        @Override
        public void onClick(View view) {
            Intent intent = ChorePagerActivity.newIntent(getActivity(), mChore.getUUID());
            Log.v("ChoreListFragment", "--onClick");
            startActivity(intent);

        }
    }

    private class ChoreAdapter extends RecyclerView.Adapter<ChoreHolder>{

        private List<Chore> mChores;

        public ChoreAdapter(List<Chore> chores){
            mChores = chores;
        }

        @Override
        public int getItemCount() {
            return mChores.size();
        }

        public void setChores(List<Chore> chores){
            mChores = chores;
        }

        @Override
        public ChoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_chore, parent, false);
            return new ChoreHolder(view);
        }

        @Override
        public void onBindViewHolder(ChoreHolder holder, int position) {
            Chore chore = mChores.get(position);
            holder.bindChore(chore);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_chore:
                Chore chore = new Chore();
                ChoreArchive.getChoreArchive(getActivity()).addChore(chore);
                Intent intent = ChorePagerActivity.newIntent(getActivity(), chore.getUUID());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        ChoreArchive choreArchive = ChoreArchive.getChoreArchive(getActivity());
        int choreCount = choreArchive.getChores().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, choreCount, choreCount);

        if (!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}