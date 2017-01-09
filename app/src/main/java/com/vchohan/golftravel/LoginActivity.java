package com.vchohan.golftravel;

import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class LoginActivity extends AppCompatActivity {

    private ImageView loginGifImageView;

    private ImageView loginDismissButton;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginGifImageView = (ImageView) findViewById(R.id.login_gif_image_view);

        Glide.with(this)
            .load(R.drawable.login_image)
            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .crossFade()
            .into(loginGifImageView);

        setupLoginPage();
    }

    private void setupLoginPage() {

        loginDismissButton = (ImageView) findViewById(R.id.login_dismiss_button);
        loginDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
