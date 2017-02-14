package com.vchohan.golftravel.golffactor;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.vchohan.golftravel.R;

public class AddGolfRoundInfoActivity extends AppCompatActivity {

    public static final String TAG = AddGolfRoundInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_golf_round_info_activity);

        createAddNewRecipeFragment(savedInstanceState);
    }

    private void createAddNewRecipeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            getSupportFragmentManager().beginTransaction().replace(R.id.new_golf_round_info_fragment_container,
                AddGolfRoundInfoFragment.newInstance(), AddGolfRoundInfoFragment.TAG).commit();
        }
    }
}
