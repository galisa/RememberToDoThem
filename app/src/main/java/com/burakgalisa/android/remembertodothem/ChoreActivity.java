package com.burakgalisa.android.remembertodothem;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Burak on 21.5.2017.
 */

public class ChoreActivity extends SingleFragmentActivity {

    private static final String EXTRA_CHORE_ID =
            "com.burakgalisa.android.remembertodothem.chore_id";

    public static Intent newIntent(Context packageContext, UUID choreId){
        Intent intent = new Intent(packageContext, ChoreActivity.class);
        intent.putExtra(EXTRA_CHORE_ID, choreId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID choreId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CHORE_ID);
        return ChoreFragment.newInstance(choreId);
    }


}
