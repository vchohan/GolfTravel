package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vchohan.baseui.CustomGauge;
import com.vchohan.baseui.ExpandableWeightLayout;

import java.util.ArrayList;
import java.util.List;

public class GolfFactorFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GolfFactorFragment.class.getSimpleName();

    private CustomGauge mWeatherGauge, mGolfFactorGauge;

    private TextView mWeatherText, mGolfFactorText;

    int i;

    private LinearLayout mSetGolfFactorButton;

    private ExpandableWeightLayout mExpandLayout;

    private ImageView mImageToggle;

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

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

        mSetGolfFactorButton = (LinearLayout) rootView.findViewById(R.id.set_golf_factor_button);
        mExpandLayout = (ExpandableWeightLayout) rootView.findViewById(R.id.expandableLayout);
        mSetGolfFactorButton.setOnClickListener(this);
        mImageToggle = (ImageView) rootView.findViewById(R.id.toggle_up_down_view);
        mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

        mViewPager = (ViewPager) rootView.findViewById(R.id.golf_factor_view_pager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) rootView.findViewById(R.id.golf_factor_tab_view);
        mTabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private void setupGaugeView(View rootView) {

        mWeatherGauge = (CustomGauge) rootView.findViewById(R.id.weather_gauge);
        mGolfFactorGauge = (CustomGauge) rootView.findViewById(R.id.golf_factor_gauge);

        mWeatherText = (TextView) rootView.findViewById(R.id.weather_text);
        mWeatherText.setText(Integer.toString(mWeatherGauge.getValue()));

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

                                    mWeatherGauge.setValue(i);
                                    mGolfFactorGauge.setValue(i);

                                    mWeatherText.setText(Integer.toString(mWeatherGauge.getValue()));
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
            case R.id.set_golf_factor_button:
                createGolfFactorView();
                mExpandLayout.toggle();
                break;
        }
    }

    private void createGolfFactorView() {
        if (mExpandLayout.isExpanded()) {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        } else {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new DateFragment(), "DATE");
        adapter.addFragment(new TimeFragment(), "TIME");
        adapter.addFragment(new CourseFragment(), "COURSE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
