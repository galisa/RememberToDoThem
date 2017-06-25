package com.burakgalisa.android.remembertodothem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Burak on 21.5.2017.
 */

public class ChoreFragment extends Fragment {

    private static final String ARG_CHORE_ID = "chore_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private Chore mChore;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mDoneCheckBox;

    public static ChoreFragment newInstance(UUID choreId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHORE_ID, choreId);

        ChoreFragment fragment = new ChoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID choreId = (UUID) getArguments().getSerializable(ARG_CHORE_ID);
        setHasOptionsMenu(true);

        mChore = ChoreArchive.getChoreArchive(getActivity()).getChore(choreId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chore, container, false);

        mTitleField = (EditText) v.findViewById(R.id.chore_title);
        mTitleField.setText(mChore.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mChore.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mDateButton = (Button) v.findViewById(R.id.chore_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mChore.getDate());
                dialog.setTargetFragment(ChoreFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });


        mDoneCheckBox = (CheckBox) v.findViewById(R.id.chore_done);
        mDoneCheckBox.setChecked(mChore.isDone());
        mDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mChore.setDone(b);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mChore.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mChore.getDate().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_chore_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete:
                ChoreArchive.getChoreArchive(getActivity()).deleteChore(mChore);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ChoreArchive.getChoreArchive(getActivity())
                .updateChore(mChore);
    }
}
