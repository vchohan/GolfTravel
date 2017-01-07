package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private TextView footerSkip, footerDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new MainPageTransformer());

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
                Toast.makeText(MainActivity.this, "skip", Toast.LENGTH_SHORT).show();
            }
        });

        footerDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
