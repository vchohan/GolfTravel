package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GolfFactorFragment extends Fragment {

    public static final String TAG = GolfFactorFragment.class.getSimpleName();

    private CustomGauge mTemperatureGauge, mGolfFactor;

    private TextView mTemperatureText;

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
        View rootView = inflater.inflate(R.layout.home_golf_factor_fragment, container, false);

        setupGaugeView(rootView);

        return rootView;
    }

    private void setupGaugeView(View rootView) {
        Button button = (Button) rootView.findViewById(R.id.button);

        mTemperatureGauge = (CustomGauge) rootView.findViewById(R.id.gauge2);
        mGolfFactor = (CustomGauge) rootView.findViewById(R.id.gauge3);

        mTemperatureText = (TextView) rootView.findViewById(R.id.textView2);
        mTemperatureText.setText(Integer.toString(mTemperatureGauge.getValue()));

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        for (i = 0; i < 100; i++) {
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mTemperatureGauge.setValue(200 + i * 5);
                                        mGolfFactor.setValue(i);

                                        mTemperatureText.setText(Integer.toString(mGolfFactor.getValue()));
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

        });
    }

}
