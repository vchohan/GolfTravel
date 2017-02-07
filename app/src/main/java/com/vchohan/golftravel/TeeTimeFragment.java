package com.vchohan.golftravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class TeeTimeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = TeeTimeFragment.class.getSimpleName();

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private LinearLayout mBookTeeTimeButton;

    public TeeTimeFragment() {
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
        View rootView = inflater.inflate(R.layout.tee_time_fragment, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.golf_factor_view_pager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) rootView.findViewById(R.id.golf_factor_tab_view);
        mTabLayout.setupWithViewPager(mViewPager);

        mBookTeeTimeButton = (LinearLayout) rootView.findViewById(R.id.book_tee_time_button);
        mBookTeeTimeButton.setOnClickListener(this);

        return rootView;
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

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.book_tee_time_button:
                launchBookTeeTime();
                break;
        }
    }

    private void launchBookTeeTime() {
        Intent bookTeeTimeIntent = new Intent(getContext(), GolfCourseFinderActivity.class);
        bookTeeTimeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bookTeeTimeIntent);
    }
}
