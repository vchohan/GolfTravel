package com.vchohan.golftravel;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

public class ScrollingActivity extends BaseActivity {

    FloatingActionMenu materialDesignFAM;

    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setupAppBar();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//                Intent intent = new Intent(ScrollingActivity.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });

        setupFloatingActionMenu();
    }

    private void setupFloatingActionMenu() {

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_settings);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_profile);
        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mwnu_logout);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked

            }
        });

    }

    public void setupAppBar() {
        BaseAppBar appBar = getAppBar();
        appBar.setTitleText(getResources().getString(R.string.home_page_title));
        appBar.setTitleTextColor(R.color.colorWhite);
        appBar.setLeftButtonIcon(R.drawable.ic_clear_white_24dp);
        appBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
