package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class AddGolfRoundInfoActivity extends BaseActivity {

    public static final String TAG = AddGolfRoundInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_golf_round_info_activity);

        addGolfRoundInfoFragment(savedInstanceState);
    }

    private void addGolfRoundInfoFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            getSupportFragmentManager().beginTransaction().replace(R.id.add_golf_round_info_frame_container,
                AddGolfRoundInfoFragment.newInstance(), AddGolfRoundInfoFragment.TAG).commit();
        }
    }
}
