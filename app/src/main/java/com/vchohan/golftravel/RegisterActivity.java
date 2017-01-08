package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RegisterActivity extends AppCompatActivity {

    private ImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        gifImageView = (ImageView) findViewById(R.id.gif_image_view);

        Glide.with(this)
            .load(R.drawable.login_image)
            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .crossFade()
            .into(gifImageView);

        createRegistrationFragment();
    }

    private void createRegistrationFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
            RegisterFragment.newInstance(), RegisterFragment.TAG).commit();
    }
}
