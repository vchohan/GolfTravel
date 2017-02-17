package com.vchohan.golftravel.teetime;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vchohan.golftravel.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class TeeTimeFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = TeeTimeFragment.class.getSimpleName();

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private LinearLayout mCurrentLocationButton, mChangeLocationLayout, mSetDateButton;

    private TextView mCurrentLocationText, mChangeLocationText, mCalendarDateText;

    private ImageView mCurrentLocationIcon, mChangeLocationIcon;

    private ImageView mImageToggle;

    private boolean isExpanded = false;

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

        mCurrentLocationButton = (LinearLayout) rootView.findViewById(R.id.current_location_button);
        mCurrentLocationButton.setOnClickListener(this);
        setupCurrentLocation(rootView);

        mChangeLocationLayout = (LinearLayout) rootView.findViewById(R.id.change_location_layout);
        mChangeLocationLayout.setOnClickListener(this);

        mChangeLocationIcon = (ImageView) rootView.findViewById(R.id.change_location_icon);
        mChangeLocationIcon.setVisibility(View.VISIBLE);

        mChangeLocationText = (TextView) rootView.findViewById(R.id.change_location_text);

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

        return rootView;
    }

    @Override
    public void onClick(final View rootView) {
        switch (rootView.getId()) {
            case R.id.current_location_button:
                setupChangeLocationCardView();
                break;
            case R.id.change_location_layout:
                Toast.makeText(getContext(), "asdfas", Toast.LENGTH_SHORT).show();
                setupChangeLocationDialog();
                break;
            case R.id.set_date_button:
                showDatePickerDialog();
                break;
            case R.id.book_tee_time_button:
                launchBookTeeTime();
                break;
        }
    }

    private void setupCurrentLocation(View rootView) {
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

                mCurrentLocationIcon = (ImageView) rootView.findViewById(R.id.current_location_icon);
                mCurrentLocationIcon.setVisibility(View.VISIBLE);

                mCurrentLocationText = (TextView) rootView.findViewById(R.id.current_location_text);
                mCurrentLocationText.setText(locationName);
                // }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void setupChangeLocationCardView() {
        if (isExpanded) {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            animateCollapseLayout();
        } else {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
            animateExpandLayout();
        }

    }

    public void animateExpandLayout() {
        if (mChangeLocationLayout != null) {
            isExpanded = true;
            TransitionManager.beginDelayedTransition(mChangeLocationLayout);
            mChangeLocationLayout.setVisibility(LinearLayout.VISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofInt(mChangeLocationLayout, "max", mChangeLocationLayout.getScrollY());
            animation.setDuration(500).start();
        }
    }

    public void animateCollapseLayout() {
        if (mChangeLocationLayout != null) {
            isExpanded = false;
            mChangeLocationLayout.setVisibility(LinearLayout.GONE);
            ObjectAnimator animation = ObjectAnimator.ofInt(mChangeLocationLayout, "max", mChangeLocationLayout.getScrollY());
            animation.setDuration(500).start();
        }
    }

    private void setupChangeLocationDialog() {
        final TextView mTitle;
        final EditText mUserInput;

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_fragment, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        mTitle = (TextView) mView.findViewById(R.id.dialog_title);
        mTitle.setText(getString(R.string.enter_city_zip_code));

        mUserInput = (EditText) mView.findViewById(R.id.user_input);

        alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogBox, int id) {
                // ToDo get user input here
                String newLocation = mUserInput.getText().toString();

                final Geocoder geocoder = new Geocoder(getContext());
                final String zipCode = newLocation;
                try {
                    List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);

                        if (addresses.size() > 0) {
                            String locationName = addresses.get(0).getLocality().toString() + ", " +
                                addresses.get(0).getAdminArea().toString() + " " +
                                addresses.get(0).getCountryName().toString();

                            Toast.makeText(getContext(), locationName, Toast.LENGTH_LONG).show();

                            mCurrentLocationText.setText(locationName);
                            animateCollapseLayout();
                        }

                        // below is for Latitude and Longitude
//                        String message = String.format("Latitude: %f, Longitude: %f",
//                            address.getLatitude(), address.getLongitude());
//                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//
//                        mCurrentLocationText.setText(message);
//                        animateCollapseLayout();

                    } else {
                        // Display appropriate message when Geocoder services are not available
                        Toast.makeText(getContext(), "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    // handle exception
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogBox, int id) {
                dialogBox.cancel();
            }
        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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

    private void launchBookTeeTime() {
        Intent bookTeeTimeIntent = new Intent(getContext(), GolfCourseFinderMapActivity.class);
        bookTeeTimeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bookTeeTimeIntent);
    }
}