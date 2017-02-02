package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GolfFactorFragment extends Fragment {

    public static final String TAG = GolfFactorFragment.class.getSimpleName();

    private CustomGauge mTemperatureGauge, mGolfFactorGauge;

    private TextView mTemperatureText, mGolfFactorText;

    int i;

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

        return rootView;
    }

    private void setupGaugeView(View rootView) {

        mTemperatureGauge = (CustomGauge) rootView.findViewById(R.id.weather_gauge);
        mGolfFactorGauge = (CustomGauge) rootView.findViewById(R.id.golf_factor_gauge);

        mTemperatureText = (TextView) rootView.findViewById(R.id.weather_text);
        mTemperatureText.setText(Integer.toString(mTemperatureGauge.getValue()));

        mGolfFactorText = (TextView) rootView.findViewById(R.id.golf_factor_text);
        mGolfFactorText.setText(Integer.toString(mGolfFactorGauge.getValue()));

        new Thread() {
            public void run() {
                for (i = 0; i < 100; i++) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mTemperatureGauge.setValue(200 + i * 5);
                                mGolfFactorGauge.setValue(i);

                                mTemperatureText.setText(Integer.toString(mTemperatureGauge.getValue()));
                                mGolfFactorText.setText(Integer.toString(mGolfFactorGauge.getValue()));
                            }
                        });
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
