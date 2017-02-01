package com.vchohan.golftravel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BaseAppBar extends LinearLayout {

    private static final String TAG = BaseAppBar.class.getSimpleName();

    private RelativeLayout mRootLayout;

    private LinearLayout mLeftButtonContainer, mRightButtonContainer;

    private ImageView mLeftButton;

    private TextView mAppBarTitle;

    private ImageView mRightButton;

    private View mAppBarDivider;

    private FirebaseAuth mAuth = null;

    private DatabaseReference mDatabaseUsers;

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
        mLeftButtonContainer = (LinearLayout) findViewById(R.id.left_button_container);
        mRightButtonContainer = (LinearLayout) findViewById(R.id.right_button_container);
        mLeftButton = (ImageView) findViewById(R.id.left_button);
        mAppBarTitle = (TextView) findViewById(R.id.app_bar_title);
        mRightButton = (ImageView) findViewById(R.id.right_button);
        mAppBarDivider = findViewById(R.id.app_bar_divider);

        initializeFirebase();
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

    public void setLeftButtonClickListener(OnClickListener clickListener) {
        mLeftButtonContainer.setOnClickListener(clickListener);
    }

    public void setRightButtonIcon(Integer drawableID) {
        setImageViewIcon(mRightButton, drawableID);
    }

    public void setRightButtonClickListener(OnClickListener clickListener) {
        mRightButtonContainer.setOnClickListener(clickListener);
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

    public void showAppBarDivider() {
        if (mAppBarDivider.getVisibility() == VISIBLE) {
            return;
        }

        mAppBarDivider.setVisibility(VISIBLE);
    }

    public void hideAppBarDivider() {
        if (mAppBarDivider.getVisibility() == GONE) {
            return;
        }

        mAppBarDivider.setVisibility(GONE);
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("user");
        mDatabaseUsers.keepSynced(true);
    }

    public void setProfilePhoto() {
        if (mAuth.getCurrentUser() != null) {
            final String userId = mAuth.getCurrentUser().getUid();
            mDatabaseUsers.child(userId).child("profilePhoto").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String image = dataSnapshot.getValue(String.class);

                    // loading nav profile image
                    Glide.with(getApplicationContext())
                        .load(image)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f)
                        .into(mRightButton);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
