package com.vchohan.golftravel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AddGolfRoundInfoFragment extends Fragment {

    public static final String TAG = AddGolfRoundInfoFragment.class.getSimpleName();

    public static final int NEW_ALARM = 1;

    private static final String DATA_RECEIVED = "data_received";

    private static final String INFORMATION = "information";

    private static final String DISCLAIMER = "disclaimer";

    private FloatingActionButton fab;

    private TextView information, disclaimer;

    private boolean dataReceived = false;

    public AddGolfRoundInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static AddGolfRoundInfoFragment newInstance() {
        AddGolfRoundInfoFragment fragment = new AddGolfRoundInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.add_golf_round_info_fragment, container, false);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewAlarmFormActivity.class);
                startActivityForResult(intent, NEW_ALARM);
            }
        });

        information = (TextView) rootView.findViewById(R.id.information);
        disclaimer = (TextView) rootView.findViewById(R.id.disclaimer);

        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        dataReceived = savedInstanceState.getBoolean(DATA_RECEIVED, false);
        if (dataReceived) {
            disclaimer.setVisibility(View.VISIBLE);
            information.setText(savedInstanceState.getString(INFORMATION));
            disclaimer.setText(savedInstanceState.getString(DISCLAIMER));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(DATA_RECEIVED, dataReceived);
        if (dataReceived) {
            savedInstanceState.putString(INFORMATION, information.getText().toString());
            savedInstanceState.putString(DISCLAIMER, disclaimer.getText().toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == NEW_ALARM && data != null) {
            if (data.hasExtra(NewAlarmFormActivity.NEW_ALARM_ADDED)
                && data.getExtras().getBoolean(NewAlarmFormActivity.NEW_ALARM_ADDED, false)) {

                // Handling the data received from the stepper form
                dataReceived = true;
                String title = data.getExtras().getString(NewAlarmFormActivity.STATE_TITLE);
                //String description = data.getExtras().getString(NewAlarmFormActivity.STATE_DESCRIPTION);
                int hour = data.getExtras().getInt(NewAlarmFormActivity.STATE_TIME_HOUR);
                int minutes = data.getExtras().getInt(NewAlarmFormActivity.STATE_TIME_MINUTES);
                String time = ((hour > 9) ? hour : ("0" + hour))
                    + ":" + ((minutes > 9) ? minutes : ("0" + minutes));
                //boolean[] weekDays = data.getExtras().getBooleanArray(NewAlarmFormActivity.STATE_WEEK_DAYS);
                information.setText("Alarm \"" + title + "\" set up at " + time);
                disclaimer.setVisibility(View.VISIBLE);
                Snackbar.make(fab, getString(R.string.new_alarm_added), Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
