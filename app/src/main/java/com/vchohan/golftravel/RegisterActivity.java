package com.vchohan.golftravel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private ImageView registerGifImageView;

    private EditText mFirstName;

    private EditText mLastName;

    private EditText mEmailField;

    private EditText mPasswordField;

    private ImageView mProfilePhoto;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseUsers;

    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        registerGifImageView = (ImageView) findViewById(R.id.register_gif_image_view);

        Glide.with(this)
            .load(R.drawable.login_image)
            .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .crossFade()
            .into(registerGifImageView);

        mFirstName = (EditText) findViewById(R.id.register_first_name);
        mLastName = (EditText) findViewById(R.id.register_last_name);
        mEmailField = (EditText) findViewById(R.id.register_email_input);
        mPasswordField = (EditText) findViewById(R.id.register_password_input);
        mProfilePhoto = (ImageView) findViewById(R.id.register_profile_photo);

        findViewById(R.id.register_button).setOnClickListener(this);

        setupAppBarDismissButton();
        initializeFirebaseAuth();

        mProgressDialog = new ProgressDialog(this);
    }

    private void initializeFirebaseAuth() {
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void createAccount(final String firstName, final String lastName, String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        mProgressDialog.setMessage("Please wait while we register your account.");
        mProgressDialog.show();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                    } else                     if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        DatabaseReference currentUserDatabase = mDatabase.child(userId);
                        currentUserDatabase.child("firstName").setValue(firstName);
                        currentUserDatabase.child("lastName").setValue(lastName);
                        currentUserDatabase.child("image").setValue("default");

                        mProgressDialog.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                }
            });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String firstName = mFirstName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String lastName = mLastName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register_button) {
            createAccount(
                mFirstName.getText().toString().trim(),
                mLastName.getText().toString().trim(),
                mEmailField.getText().toString().trim(),
                mPasswordField.getText().toString().trim());
        }
    }
}
