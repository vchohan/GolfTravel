package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    private ImageView loginDismissButton;

    /**
     * The fragment argument representing the section number for this fragment.
     */
    public LoginFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        initializeView(rootView);

        return rootView;
    }

    private void initializeView(View rootView) {

        loginDismissButton = (ImageView) rootView.findViewById(R.id.login_dismiss_button);
        loginDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

}
