package com.vchohan.golftravel.golffactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.vchohan.golftravel.CustomGauge;
import com.vchohan.golftravel.R;
import com.vchohan.golftravel.loginregister.LoginActivity;

public class GolfFactorFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GolfFactorFragment.class.getSimpleName();

    private ImageView mAddGolfRoundInfoButton;

    private LinearLayout mViewGolfRoundInfoButton, mGolfRoundInfoLayout;

    private CustomGauge mGolfFactorGauge;

    private TextView mGolfFactorText;

    int i;

    private boolean isExpanded = false;

    private ImageView mImageToggle;

    private RecyclerView mGolfRoundInfoRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;

    private FirebaseAuth mAuth = null;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabaseGolfRoundInfo;

    private DatabaseReference mDatabaseUsers;

    private DatabaseReference mDatabaseLikes;

    private boolean mProcessLike = false;

    private ProgressDialog mProgressDialog;

    public GolfFactorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.golf_factor_fragment, container, false);
        setupGaugeView(rootView);

        mAddGolfRoundInfoButton = (ImageView) rootView.findViewById(R.id.add_golf_round_info_button);
        mAddGolfRoundInfoButton.setOnClickListener(this);

        mViewGolfRoundInfoButton = (LinearLayout) rootView.findViewById(R.id.view_golf_round_info_button);
        mViewGolfRoundInfoButton.setOnClickListener(this);
        mGolfRoundInfoLayout = (LinearLayout) rootView.findViewById(R.id.golf_round_info_Layout);
        mImageToggle = (ImageView) rootView.findViewById(R.id.toggle_up_down_view);
        mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);

        initializeFirebaseAuth();
        initializeFirebaseDatabase();
        initializeRecyclerView(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupFirebaseRecyclerView();
    }

    private void setupGaugeView(View rootView) {
        mGolfFactorGauge = (CustomGauge) rootView.findViewById(R.id.golf_factor_gauge);
        mGolfFactorText = (TextView) rootView.findViewById(R.id.golf_factor_text);
        mGolfFactorText.setText(Integer.toString(mGolfFactorGauge.getValue()));

        new Thread() {
            public void run() {
                for (i = 0; i <= 100; i++) {
                    try {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGolfFactorGauge.setValue(i);
                                    mGolfFactorText.setText(Integer.toString(mGolfFactorGauge.getValue()));
                                }
                            });
                        }
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.w(TAG, "crash due to ui thread interrupted");
                    }
                }
            }
        }.start();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add_golf_round_info_button:
                setAddGolfRoundInfoView();
                break;
            case R.id.view_golf_round_info_button:
                setGolfFactorView();
                break;
        }
    }

    private void setGolfFactorView() {
        if (isExpanded) {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            animateCollapseLayout();
        } else {
            mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
            animateExpandLayout();
        }
    }

    public void animateExpandLayout() {
        if (mGolfRoundInfoLayout != null) {
            isExpanded = true;
            mGolfRoundInfoLayout.setVisibility(LinearLayout.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.jump_from_down);
            animation.setDuration(500);
            mGolfRoundInfoLayout.setAnimation(animation);
            mGolfRoundInfoLayout.animate();
            animation.start();
        }
    }

    public void animateCollapseLayout() {
        if (mGolfRoundInfoLayout != null) {
            isExpanded = false;
            mGolfRoundInfoLayout.setVisibility(LinearLayout.GONE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.jump_to_down);
            animation.setDuration(500);
            mGolfRoundInfoLayout.setAnimation(animation);
            mGolfRoundInfoLayout.animate();
            animation.start();
        }
    }

    private void setAddGolfRoundInfoView() {
        Intent golfRoundInfoIntent = new Intent(getContext(), AddGolfRoundInfoActivity.class);
        golfRoundInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(golfRoundInfoIntent);

    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
    }

    private void initializeFirebaseDatabase() {

        mDatabaseGolfRoundInfo = FirebaseDatabase.getInstance().getReference().child("GolfRoundInfo");
        mDatabaseGolfRoundInfo.keepSynced(true);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLikes.keepSynced(true);

    }

    private void initializeRecyclerView(View rootView) {
        //setup golf round info recyclerView
        mGolfRoundInfoRecyclerView = (RecyclerView) rootView.findViewById(R.id.golf_round_info_recycler_view);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mGolfRoundInfoRecyclerView.setHasFixedSize(true);
        mGolfRoundInfoRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private void setupFirebaseRecyclerView() {
        showProgressDialog();

        mAuth.addAuthStateListener(mAuthListener);

        // user mDatabaseGolfRoundInfo for all users, else mQueryCurrentUser for logged in single user
        final FirebaseRecyclerAdapter<GolfRoundInfoUtils, GolfRoundInfoViewHolder> firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<GolfRoundInfoUtils,
                GolfRoundInfoViewHolder>(GolfRoundInfoUtils.class, R.layout.golf_round_info_card_view, GolfRoundInfoViewHolder.class,
                mDatabaseGolfRoundInfo) {
                @Override
                protected void populateViewHolder(GolfRoundInfoViewHolder viewHolder, GolfRoundInfoUtils model, int position) {

                    final String golfRoundInfoKey = getRef(position).getKey();

                    viewHolder.setImage(getContext(), model.getImage());
                    viewHolder.setName(model.getName());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setTime(model.getTime());

                    viewHolder.setLikeButton(golfRoundInfoKey);

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        Intent golfRoundInfoDetailsIntent = new Intent(getContext(), GolfRoundInfoDetailsActivity.class);
//                        golfRoundInfoDetailsIntent.putExtra("golfRoundInfoId", golfRoundInfoKey);
//                        golfRoundInfoDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(golfRoundInfoDetailsIntent);
                        }
                    });

                    viewHolder.mLikeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mProcessLike = true;

                            mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mProcessLike) {
                                        if (dataSnapshot.child(golfRoundInfoKey).hasChild(mAuth.getCurrentUser().getUid())) {
                                            mDatabaseLikes.child(golfRoundInfoKey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessLike = false;
                                        } else {
                                            mDatabaseLikes.child(golfRoundInfoKey).child(mAuth.getCurrentUser().getUid())
                                                .setValue("RandomValue");
                                            mProcessLike = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                }
                            });
                        }
                    });
                    hideProgressDialog();
                }
            };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                    (positionStart >= (friendlyMessageCount - 1) &&
                        lastVisiblePosition == (positionStart - 1))) {
                    mLinearLayoutManager.scrollToPosition(positionStart);
                }
            }
        });
        mGolfRoundInfoRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
