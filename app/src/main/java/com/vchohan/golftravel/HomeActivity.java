package com.vchohan.golftravel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends BaseActivity {

    private FirebaseAuth mAuth = null;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabaseUsers;

    private static final int PERMISSION_REQUEST_CODE = 200;

    private CoordinatorLayout coordinatorLayout;

    private FloatingActionMenu mFloatingActionMenu;

    private FloatingActionButton mSettings, mProfile, mLogout;

    private CustomGauge mTemperatureGauge, mGolfFactor;

    private TextView mTemperatureText;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing Facebook SDK, must initialize before setContentView
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.home_activity);

        setupAppBar();
        initializeFirebase();
//        requestLocationPermission();
//        locationPermission();

        setupGaugeView();

        setupFloatingActionMenu();
        createCustomAnimation();

        logoutFacebook();
    }

    public void setupAppBar() {
        BaseAppBar appBar = getAppBar();
        appBar.setTitleText(getResources().getString(R.string.home_page_title));
        appBar.setTitleTextColor(R.color.colorRed900);
        appBar.setProfilePhoto();
        appBar.showAppBarDivider();
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("user");
        mDatabaseUsers.keepSynced(true);
    }

    //TODO: Implement View Pager here

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

    private void locationPermission() {
        if (checkLocationPermission()) {
            Snackbar.make(coordinatorLayout, "Permission already granted.", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(coordinatorLayout, "Please request permission.", Snackbar.LENGTH_LONG).show();
        }

        if (!checkLocationPermission()) {
            requestLocationPermission();
        } else {
            Snackbar.make(coordinatorLayout, "Permission already granted.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
    }

    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {
                        Snackbar.make(coordinatorLayout, "Permission Granted, GolfTravel can now access location data.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Snackbar.make(coordinatorLayout, "Permission Denied, GolfTravel cannot access location data.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("Allow access to location permission in order to retrieve accurate data.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                    PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                                return;
                            }
                        }

                    }
                }

                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show();
    }

    private void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        finish();
    }

    private void logoutFacebook() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.putExtra("logout", true);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
