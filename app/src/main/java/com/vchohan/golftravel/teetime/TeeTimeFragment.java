package com.vchohan.golftravel.teetime;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vchohan.golftravel.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class TeeTimeFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = TeeTimeFragment.class.getSimpleName();

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private LinearLayout mSetLocationButton, mSetDateButton, mCalendarLayout, mBookTeeTimeButton;

    private TextView mCurrentLocationText, mCalendarDateText;

    private ImageView mAddCurrentLocationIcon, mCurrentLocationIcon;

    private ImageView mImageToggle;

    private boolean isExpanded = false;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

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
        final View rootView = inflater.inflate(R.layout.teetime_fragment, container, false);

        mSetLocationButton = (LinearLayout) rootView.findViewById(R.id.set_location_button);
        mSetLocationButton.setOnClickListener(this);

        mCurrentLocationText = (TextView) rootView.findViewById(R.id.current_location_text);

        mAddCurrentLocationIcon = (ImageView) rootView.findViewById(R.id.add_current_location_icon);
        mAddCurrentLocationIcon.setVisibility(View.VISIBLE);
        mCurrentLocationIcon = (ImageView) rootView.findViewById(R.id.current_location_icon);

//        mTabLayout = (TabLayout) rootView.findViewById(R.id.golf_factor_tab_view);
//        mTabLayout.setupWithViewPager(mViewPager);
//
//        mViewPager = (ViewPager) rootView.findViewById(R.id.golf_factor_view_pager);
//        setupViewPager(mViewPager);
//
//        mBookTeeTimeButton = (LinearLayout) rootView.findViewById(R.id.book_tee_time_button);
//        mBookTeeTimeButton.setOnClickListener(this);

        mSetDateButton = (LinearLayout) rootView.findViewById(R.id.set_date_button);
        mSetDateButton.setOnClickListener(this);

        mCalendarDateText = (TextView) rootView.findViewById(R.id.calendar_date_text);

        mImageToggle = (ImageView) rootView.findViewById(R.id.toggle_up_down_view);
        mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);

        mCalendarLayout = (LinearLayout) rootView.findViewById(R.id.calendar_Layout);

        return rootView;
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
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
            case R.id.set_date_button:
                showDatePickerDialog();
//                setCalendarDate();
                break;
            case R.id.book_tee_time_button:
                launchBookTeeTime();
                break;
        }
    }

    private void setCurrentLocation(View rootView) {
        //Location Manager is used to figure out which location provider needs to be used.
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        //Best location provider is decided by the criteria
        Criteria criteria = new Criteria();

        //location manager will take the best location from the criteria
        locationManager.getBestProvider(criteria, true);

        //nce you know the name of the LocationProvider, you can call getLastKnownPosition() to find out where you were recently.
        if (ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // requestPermissions
            requestLocationPermission();
            locationPermission(rootView);

            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Log.d("Tag", "1");
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                //while(locTextView.getText().toString()=="Location") {
                String locationName = addresses.get(0).getLocality().toString() + ", " +
                    addresses.get(0).getAdminArea().toString() + " " +
                    addresses.get(0).getCountryName().toString();
                mCurrentLocationText.setText(locationName);
                mAddCurrentLocationIcon.setVisibility(View.GONE);
                mCurrentLocationIcon.setVisibility(View.VISIBLE);
                // }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
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

    private void setCalendarDate() {
        if (isExpanded) {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            animateCollapseLayout();
        } else {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
            animateExpandLayout();
        }
    }

    public void animateExpandLayout() {
        if (mCalendarLayout != null) {
            isExpanded = true;
            mCalendarLayout.setVisibility(LinearLayout.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.jump_from_down);
            animation.setDuration(500);
            mCalendarLayout.setAnimation(animation);
            mCalendarLayout.animate();
            animation.start();
        }
    }

    public void animateCollapseLayout() {
        if (mCalendarLayout != null) {
            isExpanded = false;
            mCalendarLayout.setVisibility(LinearLayout.GONE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.jump_to_down);
            animation.setDuration(500);
            mCalendarLayout.setAnimation(animation);
            mCalendarLayout.animate();
            animation.start();
        }
    }

    private void launchBookTeeTime() {
        Intent bookTeeTimeIntent = new Intent(getContext(), GolfCourseFinderMapActivity.class);
        bookTeeTimeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bookTeeTimeIntent);
    }
}