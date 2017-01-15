package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SomeActivity extends AppCompatActivity {

    public static final String TAG = SomeActivity.class.getSimpleName();

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private TextView footerSkip, footerDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.some_activity);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new SomeViewPagerAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new SomePageTransformer());

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager, true);

        footerSkip = (TextView) findViewById(R.id.footer_skip);
        footerDone = (TextView) findViewById(R.id.footer_done);

        setupText();
    }

    private void setupText() {

        footerSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SomeActivity.this, "skip", Toast.LENGTH_SHORT).show();
            }
        });

        footerDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SomeActivity.this, "done", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
