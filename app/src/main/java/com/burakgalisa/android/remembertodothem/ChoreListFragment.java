package com.burakgalisa.android.remembertodothem;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ChoreListFragment extends Fragment {

    private RecyclerView mChoreRecyclerView;
    private ChoreAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_chore_list, container, false);

        mChoreRecyclerView = (RecyclerView) view.findViewById(R.id.chore_recycler_view);
        mChoreRecyclerView.setHasFixedSize(true);
        mChoreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ChoreArchive choreArchive = ChoreArchive.getChoreArchive(getActivity());
        List<Chore> chores = choreArchive.getChores();

        if (mAdapter == null){
            mAdapter = new ChoreAdapter(chores);
            mChoreRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }

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

}