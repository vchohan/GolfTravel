package com.vchohan.golftravel;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    @VisibleForTesting
    public ImageView mLoginRegisterLayoutBackground;

    @VisibleForTesting
    public ImageView appBarDismissButton;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.progress_bar_loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void setupLoginRegisterBackgroundGif() {
        mLoginRegisterLayoutBackground = (ImageView) findViewById(R.id.gif_image_view);
        Glide.with(this)
            .load(R.drawable.login_image)
            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .crossFade()
            .into(mLoginRegisterLayoutBackground);
    }

    public BaseAppBar getAppBar() {
        return (BaseAppBar) findViewById(R.id.app_bar);
    }
}
