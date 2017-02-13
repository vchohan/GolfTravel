package com.vchohan.golftravel.golffactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vchohan.golftravel.CustomGauge;
import com.vchohan.golftravel.R;

public class GolfFactorFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GolfFactorFragment.class.getSimpleName();

    private ImageView mAddGolfRoundInfoButton;

    private LinearLayout mViewGolfRoundInfoButton, mGolfRoundInfoLayout;

    private CustomGauge mGolfFactorGauge;

    private TextView mGolfFactorText;

    int i;

    private boolean isExpanded = false;

    private ImageView mImageToggle;


    public GolfFactorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.golf_factor_fragment, container, false);
        setupGaugeView(rootView);

        mAddGolfRoundInfoButton = (ImageView) rootView.findViewById(R.id.add_golf_round_info_button);
        mAddGolfRoundInfoButton.setOnClickListener(this);

        mViewGolfRoundInfoButton = (LinearLayout) rootView.findViewById(R.id.view_golf_round_info_button);
        mViewGolfRoundInfoButton.setOnClickListener(this);
        mGolfRoundInfoLayout = (LinearLayout) rootView.findViewById(R.id.golf_round_info_Layout);
        mImageToggle = (ImageView) rootView.findViewById(R.id.toggle_up_down_view);
        mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);

        return rootView;
    }

    private void setupGaugeView(View rootView) {
        mGolfFactorGauge = (CustomGauge) rootView.findViewById(R.id.golf_factor_gauge);
        mGolfFactorText = (TextView) rootView.findViewById(R.id.golf_factor_text);
        mGolfFactorText.setText(Integer.toString(mGolfFactorGauge.getValue()));

        new Thread() {
            public void run() {
                for (i = 0; i <= 100; i++) {
                    try {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGolfFactorGauge.setValue(i);
                                    mGolfFactorText.setText(Integer.toString(mGolfFactorGauge.getValue()));
                                }
                            });
                        }
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.w(TAG, "crash due to ui thread interrupted");
                    }
                }
            }
        }.start();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add_golf_round_info_button:
                setAddGolfRoundInfoView();
                break;
            case R.id.view_golf_round_info_button:
                setGolfFactorView();
                break;
        }
    }

    private void setGolfFactorView() {
        if (isExpanded) {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            animateCollapseLayout();
        } else {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
            animateExpandLayout();
        }
    }

    public void animateExpandLayout() {
        if (mGolfRoundInfoLayout != null) {
            isExpanded = true;
            mGolfRoundInfoLayout.setVisibility(LinearLayout.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.jump_from_down);
            animation.setDuration(500);
            mGolfRoundInfoLayout.setAnimation(animation);
            mGolfRoundInfoLayout.animate();
            animation.start();
        }
    }

    public void animateCollapseLayout() {
        if (mGolfRoundInfoLayout != null) {
            isExpanded = false;
            mGolfRoundInfoLayout.setVisibility(LinearLayout.GONE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.jump_to_down);
            animation.setDuration(500);
            mGolfRoundInfoLayout.setAnimation(animation);
            mGolfRoundInfoLayout.animate();
            animation.start();
        }
    }

    private void setAddGolfRoundInfoView() {
//        // launch a fragment from a fragment
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        AddGolfRoundInfoFragment addGolfRoundInfoFragment = new AddGolfRoundInfoFragment();
//        transaction.add(R.id.frame_container, addGolfRoundInfoFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();

        Intent golfRoundInfoIntent = new Intent(getContext(), GolfRoundInfoActivity.class);
        golfRoundInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(golfRoundInfoIntent);

    }

}
