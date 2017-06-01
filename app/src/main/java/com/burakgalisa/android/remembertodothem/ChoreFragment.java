package com.burakgalisa.android.remembertodothem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Burak on 21.5.2017.
 */

public class ChoreFragment extends Fragment {

    private static final String ARG_CHORE_ID = "chore_id";
    private static final String DIALOG_DATE = "DialogDate";

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
        mDateButton.setText(mChore.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mChore.getDate());
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
}
