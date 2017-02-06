package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddGolfRoundInfoFragment extends Fragment {

    public static final String TAG = AddGolfRoundInfoFragment.class.getSimpleName();

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


        return rootView;
    }



}
