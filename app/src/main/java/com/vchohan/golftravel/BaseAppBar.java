package com.vchohan.golftravel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseAppBar extends LinearLayout {

    private static final String TAG = BaseAppBar.class.getSimpleName();

    private RelativeLayout mRootLayout;

    private ImageView mLeftButton;

    private TextView mAppBarTitle;

    private ImageView mRightButton;


    public BaseAppBar(Context context) {
        super(context);
        inti(context);
    }

    public BaseAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inti(context);
    }

    public BaseAppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inti(context);
    }

    private void inti(Context context) {
       inflate(context, R.layout.base_app_bar, this);

        mRootLayout = (RelativeLayout) findViewById(R.id.app_bar_root_layout);
        mLeftButton = (ImageView) findViewById(R.id.left_button);
        mAppBarTitle = (TextView) findViewById(R.id.app_bar_title);
        mRightButton = (ImageView) findViewById(R.id.right_button);
    }

    public void setImageViewIcon(ImageView imageView, Integer drawableID) {
        if (drawableID != null) {
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawableID, null));
        } else {
            imageView.setImageDrawable(null);
        }
    }

    public void setLeftButtonIcon(Integer drawableID) {
        setImageViewIcon(mLeftButton, drawableID);
    }

    public void setRightButtonIcon(Integer drawableID) {
        setImageViewIcon(mRightButton, drawableID);
    }

    public void setTitleText(String title) {
        if (title == null) {
            this.mAppBarTitle.setVisibility(GONE);
        } else {
            this.mAppBarTitle.setVisibility(VISIBLE);
        }

        this.mAppBarTitle.setText(title);
    }

    public void setTitleTextColor(int colorID) {
        mAppBarTitle.setTextColor(ContextCompat.getColor(getContext(), colorID));
    }
}
