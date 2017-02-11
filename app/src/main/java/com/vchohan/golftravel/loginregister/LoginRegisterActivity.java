package com.vchohan.golftravel.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vchohan.golftravel.BaseActivity;
import com.vchohan.golftravel.R;

public class LoginRegisterActivity extends BaseActivity {

    public static final String TAG = LoginRegisterActivity.class.getSimpleName();

    private Button loginPageButton, registerPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);

        setupLoginRegisterBackgroundGif();

        loginPageButton = (Button) findViewById(R.id.login_page_button);
        loginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginRegisterActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        });

        registerPageButton = (Button) findViewById(R.id.register_page_button);
        registerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });
    }
}
