package com.vchohan.golftravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RegisterFragment extends android.support.v4.app.Fragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();

    private ImageView registerDismissButton;

    /**
     * The fragment argument representing the section number for this fragment.
     */
    public RegisterFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_fragment, container, false);
        initializeView(rootView);

        return rootView;
    }

    private void initializeView(View rootView) {

        registerDismissButton = (ImageView) rootView.findViewById(R.id.register_dismiss_button);
        registerDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }

}
