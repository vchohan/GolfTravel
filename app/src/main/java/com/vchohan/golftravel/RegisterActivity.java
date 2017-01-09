package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RegisterActivity extends AppCompatActivity {

    private ImageView registerGifImageView;

    private ImageView registerDismissButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        registerGifImageView = (ImageView) findViewById(R.id.register_gif_image_view);

        Glide.with(this)
            .load(R.drawable.login_image)
            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .crossFade()
            .into(registerGifImageView);

        setupRegisterPage();
    }

    private void setupRegisterPage() {

        registerDismissButton = (ImageView) findViewById(R.id.register_dismiss_button);
        registerDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
