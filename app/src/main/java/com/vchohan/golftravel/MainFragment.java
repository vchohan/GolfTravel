package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by vchohan on 1/6/17.
 */

public class MainFragment extends Fragment {

    private static final String BACKGROUND_COLOR = "backgroundColor";

    private static final String PAGE = "page";

    private int mBackgroundColor, mPage;

//    private TextView title, description;

    public static MainFragment newInstance(int backgroundColor, int page) {
        MainFragment frag = new MainFragment();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, backgroundColor);
        b.putInt(PAGE, page);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(BACKGROUND_COLOR)) {
            throw new RuntimeException("Fragment must contain a \"" + BACKGROUND_COLOR + "\" argument!");
        }
        mBackgroundColor = getArguments().getInt(BACKGROUND_COLOR);

        if (!getArguments().containsKey(PAGE)) {
            throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
        }
        mPage = getArguments().getInt(PAGE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the background color of the root view to the color specified in newInstance()
        View background = view.findViewById(R.id.intro_background);
        background.setBackgroundColor(mBackgroundColor);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Select a layout based on the current page
        int layoutResId;
        switch (mPage) {
            case 0:
                layoutResId = R.layout.main_fragment;
                break;
            case 1:
                layoutResId = R.layout.main_fragment;
                break;
            default:
                layoutResId = R.layout.main_fragment;
        }

        // Inflate the layout resource file
        View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);

        // Set the current page index as the View's tag (useful in the PageTransformer)
        view.setTag(mPage);

        TextView titleText = (TextView) view.findViewById(R.id.title);

        TextView descriptionText = (TextView) view.findViewById(R.id.description);

        return view;
    }
}
