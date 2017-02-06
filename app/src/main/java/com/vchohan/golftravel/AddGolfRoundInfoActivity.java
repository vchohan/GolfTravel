package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class AddGolfRoundInfoActivity extends BaseActivity {

    public static final String TAG = AddGolfRoundInfoActivity.class.getSimpleName();

    private BaseAppBar mBaseAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_golf_round_info_activity);

        setupAppBar();
        addGolfRoundInfoFragment(savedInstanceState);
    }

    public void setupAppBar() {
        mBaseAppBar = getAppBar();
        mBaseAppBar.setBackgroundColor(getResources().getColor(R.color.colorRed500));
        mBaseAppBar.setLeftButtonIcon(R.drawable.ic_clear_white_24dp);
        mBaseAppBar.setLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBaseAppBar.setTitleText(getResources().getString(R.string.add_golf_round_info_title));
        mBaseAppBar.setTitleTextColor(R.color.colorWhite);
        mBaseAppBar.setProfilePhoto();
        mBaseAppBar.showAppBarDivider();
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
