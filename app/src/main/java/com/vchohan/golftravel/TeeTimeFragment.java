package com.vchohan.golftravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by vchohan on 2/1/17.
 */

public class TeeTimeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = TeeTimeFragment.class.getSimpleName();

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

        mBookTeeTimeButton = (LinearLayout) rootView.findViewById(R.id.book_tee_time_button);
        mBookTeeTimeButton.setOnClickListener(this);

        return rootView;
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
        Intent bookTeeTimeIntent = new Intent(getContext(), MapsActivity.class);
        bookTeeTimeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bookTeeTimeIntent);
    }
}
