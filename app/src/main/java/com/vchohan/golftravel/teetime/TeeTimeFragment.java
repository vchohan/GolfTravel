package com.vchohan.golftravel.teetime;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vchohan.golftravel.R;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.facebook.FacebookSdk.getApplicationContext;

public class TeeTimeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = TeeTimeFragment.class.getSimpleName();

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private LinearLayout mSetLocationButton, mBookTeeTimeButton;

    private TextView mCurrentLocationText;

    private ImageView mAddCurrentLocationIcon, mCurrentLocationIcon;

    private static final int PERMISSION_REQUEST_CODE = 200;

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

        mSetLocationButton = (LinearLayout) rootView.findViewById(R.id.set_location_button);
        mSetLocationButton.setOnClickListener(this);

        mCurrentLocationText = (TextView) rootView.findViewById(R.id.current_location_text);
        mCurrentLocationText.setText("Set Your Current Location");

        mAddCurrentLocationIcon = (ImageView) rootView.findViewById(R.id.add_current_location_icon);
        mAddCurrentLocationIcon.setVisibility(View.VISIBLE);
        mCurrentLocationIcon = (ImageView) rootView.findViewById(R.id.current_location_icon);

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
    public void onClick(final View rootView) {
        switch (rootView.getId()) {
            case R.id.set_location_button:
                setCurrentLocation(rootView);
                break;
            case R.id.book_tee_time_button:
                launchBookTeeTime();
                break;
        }
    }

    private void setCurrentLocation(View rootView) {
        mAddCurrentLocationIcon.setVisibility(View.GONE);
        mCurrentLocationIcon.setVisibility(View.VISIBLE);

        requestLocationPermission();
        locationPermission(rootView);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private void locationPermission(View rootView) {
        if (checkLocationPermission()) {
            Snackbar.make(rootView, "Permission already granted.", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(rootView, "Please request permission.", Snackbar.LENGTH_LONG).show();
        }

        if (!checkLocationPermission()) {
            requestLocationPermission();
        } else {
            Snackbar.make(rootView, "Permission already granted.", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    View rootView = null;

                    if (locationAccepted) {
                        Snackbar.make(rootView, "Permission Granted, GolfTravel can now access location data.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Snackbar.make(rootView, "Permission Denied, GolfTravel cannot access location data.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("Allow access to location permission in order to retrieve accurate data.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                    PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                                return;
                            }
                        }

                    }
                }

                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show();
    }

    private void launchBookTeeTime() {
        Intent bookTeeTimeIntent = new Intent(getContext(), GolfCourseFinderMapActivity.class);
        bookTeeTimeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bookTeeTimeIntent);
    }
}