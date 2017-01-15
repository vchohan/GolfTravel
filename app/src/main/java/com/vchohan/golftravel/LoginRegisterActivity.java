package com.vchohan.golftravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class LoginRegisterActivity extends AppCompatActivity {

    public static final String TAG = LoginRegisterActivity.class.getSimpleName();

    private ImageView gifImageView;

    private Button loginPageButton, registerPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);

        gifImageView = (ImageView) findViewById(R.id.gif_image_view);

        Glide.with(this)
            .load(R.drawable.login_image)
            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .crossFade()
            .into(gifImageView);

        loginPageButton = (Button) findViewById(R.id.login_page_button);
        loginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginRegisterActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        });

        registerPageButton = (Button) findViewById(R.id.register_page_button);
        registerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });
    }
}
