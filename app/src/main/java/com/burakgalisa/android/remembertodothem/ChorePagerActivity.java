package com.burakgalisa.android.remembertodothem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by Burak on 22.5.2017.
 */
public class ChorePagerActivity extends FragmentActivity{

    private static final String EXTRA_CHORE_ID =
            "com.burakgalisa.android.remembertodothem.chore_id";

    private ViewPager mViewPager;
    private List<Chore> mChores;

    public static Intent newIntent(Context packageContext, UUID choreID){
        Intent intent = new Intent(packageContext, ChorePagerActivity.class);
        intent.putExtra(EXTRA_CHORE_ID, choreID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_pager);

        Log.v("ChorePagerActivity", "--onCreate");

        UUID choreId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CHORE_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_chore_pager_view_pager);

        mChores = ChoreArchive.getChoreArchive(this).getChores();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Chore chore = mChores.get(position);
                return ChoreFragment.newInstance(chore.getUUID());
            }


            @Override
            public int getCount() {
                return mChores.size();
            }
        });

        for (int i = 0; i < mChores.size(); i++){
            if (mChores.get(i).getUUID().equals(choreId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}