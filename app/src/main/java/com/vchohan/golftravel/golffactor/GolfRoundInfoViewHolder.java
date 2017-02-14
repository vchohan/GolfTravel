package com.vchohan.golftravel.golffactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vchohan.golftravel.R;

public class GolfRoundInfoViewHolder extends RecyclerView.ViewHolder {

    View mView;

    ImageView mRemoveButton, mLikeButton;

    DatabaseReference mDatabaseLikes;

    FirebaseAuth mAuth;

    public GolfRoundInfoViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        mRemoveButton = (ImageView) mView.findViewById(R.id.remove_button);

        mLikeButton = (ImageView) mView.findViewById(R.id.like_button);

        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth = FirebaseAuth.getInstance();

        mDatabaseLikes.keepSynced(true);
    }

    public void setImage(Context context, String image) {
        ImageView golfCourseImage = (ImageView) mView.findViewById(R.id.golf_course_image);
        Glide.with(context).load(image).into(golfCourseImage);
    }

    public void setName(String title) {
        TextView golfCourseTitle = (TextView) mView.findViewById(R.id.golf_course_name);
        golfCourseTitle.setText(title);
    }

    public void setDate(String date) {
        TextView golfPlayDate = (TextView) mView.findViewById(R.id.golf_play_date);
        golfPlayDate.setText(date);
    }

    public void setTime(String time) {
        TextView golfPlayTime = (TextView) mView.findViewById(R.id.golf_play_time);
        golfPlayTime.setText(time);
    }

    public void setLikeButton(final String golfRoundInfoKey) {
        mDatabaseLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(golfRoundInfoKey).hasChild(mAuth.getCurrentUser().getUid())) {
                    mLikeButton.setImageResource(R.drawable.ic_access_time_black_24dp);
                } else {
                    mLikeButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
