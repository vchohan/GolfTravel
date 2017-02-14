package com.vchohan.golftravel.loginregister;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vchohan.golftravel.CameraUtility;
import com.vchohan.golftravel.CircleTransform;
import com.vchohan.golftravel.BaseActivity;
import com.vchohan.golftravel.BaseAppBar;
import com.vchohan.golftravel.main.MainActivity;
import com.vchohan.golftravel.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mFirstName;

    private EditText mLastName;

    private EditText mEmailField;

    private EditText mPasswordField;

    private ImageView mProfilePhoto;

    private static final int CAMERA_REQUEST = 0;

    private static final int GALLERY_REQUEST = 1;

    private static final String URI = "uri";

    private Uri mImageUri = null;

    private String selectImageOptions;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseUsers;

    private StorageReference mStorage;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        setupAppBar();
        setupLoginRegisterBackgroundGif();
        initializeFirebaseAuth();

        mFirstName = (EditText) findViewById(R.id.register_first_name);
        mLastName = (EditText) findViewById(R.id.register_last_name);
        mEmailField = (EditText) findViewById(R.id.register_email_input);
        mPasswordField = (EditText) findViewById(R.id.register_password_input);
        mProfilePhoto = (ImageView) findViewById(R.id.register_profile_photo);
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoFrom();
            }
        });

        if (savedInstanceState != null) {
            mImageUri = savedInstanceState.getParcelable(URI);
            mProfilePhoto.setImageURI(mImageUri);
        }

        findViewById(R.id.register_button).setOnClickListener(this);

    }

    public void setupAppBar() {
        BaseAppBar appBar = getAppBar();
        appBar.setTitleText(getResources().getString(R.string.register_page_title));
        appBar.setTitleTextColor(R.color.colorWhite);
        appBar.setLeftButtonIcon(R.drawable.ic_clear_white_24dp);
        appBar.setLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("user");
        mStorage = FirebaseStorage.getInstance().getReference().child("profilePhoto");
    }

    private void createAccount(final String firstName, final String lastName, String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        mProgressDialog.setMessage("Please wait while we register your account.");
        mProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // if sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                } else if (task.isSuccessful()) {
                    // look for uid and set user first and last name.
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDatabase = mDatabaseUsers.child(userId);
                    currentUserDatabase.child("firstName").setValue(firstName);
                    currentUserDatabase.child("lastName").setValue(lastName);

                    // method for setting user profile photo.
                    submitProfilePhoto();
                }
                hideProgressDialog();
            }
        });
    }

    private void submitProfilePhoto() {
        final String userId = mAuth.getCurrentUser().getUid();

        if (mImageUri != null) {
            showProgressDialog();

            StorageReference filePath = mStorage.child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    mDatabaseUsers.child(userId).child("profilePhoto").setValue(downloadUri);

                    mProgressDialog.dismiss();

                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                    Toast.makeText(RegisterActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String toastText = "No photo was selected";
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String firstName = mFirstName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            mFirstName.setError("Required.");
            valid = false;
        } else {
            mFirstName.setError(null);
        }

        String lastName = mLastName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            mLastName.setError("Required.");
            valid = false;
        } else {
            mLastName.setError(null);
        }

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else if (!isEmailValid(email)) {
            mEmailField.setError(getString(R.string.error_invalid_email));
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordField.setError(getString(R.string.error_invalid_password));
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void selectPhotoFrom() {
        // check and request camera and gallery permissions
        if (checkPermission()) {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please request permission.", Toast.LENGTH_LONG).show();
        }

        if (!checkPermission()) {
            requestPermission();
        } else {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show();
        }

        // open select photo options alert dialog
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CameraUtility.checkPermission(RegisterActivity.this);
                if (items[item].equals("Take Photo")) {
                    selectImageOptions = "Take Photo";
                    if (result) {
                        cameraIntent();
                    }
                } else if (items[item].equals("Choose from Library")) {
                    selectImageOptions = "Choose from Library";
                    if (result) {
                        galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, GALLERY_REQUEST);
            }
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST || requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            CropImage.activity(mImageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(getApplicationContext()).load(resultUri)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mProfilePhoto);

                // Loading profile image
                Glide.with(getApplicationContext()).load(resultUri)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mProfilePhoto);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, "Crop Image:", error);
            }
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mProfilePhoto != null) {
            outState.putParcelable(URI, mImageUri);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED
            && result1 == PackageManager.PERMISSION_GRANTED
            && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
            PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    String permissionCameraGranted = "Permission Granted. GolfTravel can now access camera.";
                    String permissonReadWriteGranted = "Permission Granted. GolfTravel can now access gallery.";
                    String permissionDenied = "Camera or Gallery Permission Denied. Please visit app settings to allow permissions";
                    if (cameraAccepted) {
                        Toast.makeText(this, permissionCameraGranted, Toast.LENGTH_LONG).show();
                    } else if (readAccepted && writeAccepted) {
                        Toast.makeText(this, permissonReadWriteGranted, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, permissionDenied, Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("Allow access to camera or gallery to take a photo.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                    PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(RegisterActivity.this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show();
    }
}
