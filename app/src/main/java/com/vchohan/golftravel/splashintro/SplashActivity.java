package com.vchohan.golftravel.splashintro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.vchohan.golftravel.R;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();

    //splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

//        startAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this, IntroActivity.class);
                splashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(splashIntent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
