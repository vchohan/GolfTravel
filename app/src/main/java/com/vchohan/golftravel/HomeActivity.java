package com.vchohan.golftravel;

import com.google.firebase.auth.FirebaseAuth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class HomeActivity extends BaseActivity {

    FloatingActionMenu mFloatingActionMenu;

    FloatingActionButton mSettings, mProfile, mLogout;

    private FirebaseAuth mAuth = null;

    private CustomGauge mTemperatureGauge, mGolfFactor;

    private TextView mTemperatureText;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        setupAppBar();

        mAuth = FirebaseAuth.getInstance();

        setupGaugeView();

        setupFloatingActionMenu();
        createCustomAnimation();
    }

    private void setupGaugeView() {
        Button button = (Button) findViewById(R.id.button);

        mTemperatureGauge = (CustomGauge) findViewById(R.id.gauge2);
        mGolfFactor = (CustomGauge) findViewById(R.id.gauge3);

        mTemperatureText = (TextView) findViewById(R.id.textView2);
        mTemperatureText.setText(Integer.toString(mTemperatureGauge.getValue()));

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        for (i = 0; i < 100; i++) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mTemperatureGauge.setValue(200 + i * 5);
                                        mGolfFactor.setValue(i);

                                        mTemperatureText.setText(Integer.toString(mGolfFactor.getValue()));
                                    }
                                });
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }

        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setupAppBar() {
        BaseAppBar appBar = getAppBar();
        appBar.setTitleText(getResources().getString(R.string.home_page_title));
        appBar.setTitleTextColor(R.color.colorRed900);
        appBar.setRightButtonIcon(R.drawable.circular_image);
        appBar.showAppBarDivider();
    }

    private void setupFloatingActionMenu() {
        mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        mSettings = (FloatingActionButton) findViewById(R.id.menu_settings);
        mProfile = (FloatingActionButton) findViewById(R.id.menu_profile);
        mLogout = (FloatingActionButton) findViewById(R.id.mwnu_logout);

        mSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked

            }
        });
        mProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//TODO something when floating action menu second item clicked

            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();

            }
        });
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(mFloatingActionMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(mFloatingActionMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(mFloatingActionMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(mFloatingActionMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFloatingActionMenu.getMenuIconView().setImageResource(mFloatingActionMenu.isOpened()
                    ? R.drawable.ic_keyboard_arrow_down_white_24dp : R.drawable.ic_keyboard_arrow_up_white_24dp);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        mFloatingActionMenu.setIconToggleAnimatorSet(set);
    }

    private void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        finish();
    }
}
