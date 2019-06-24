package com.mazetireal.chat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;
        public TextView timeText;

        public MessageViewHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);
            timeText = view.findViewById(R.id.time_text_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();
        long message_time = c.getTime();
        Log.d("APPTAG", "Text: " + message_time);
//        Date myDate = new Date(message_time*1000);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String finalDate = outputFormat.format(message_time);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.displayName.setText(name);

                Picasso.get().load(image)
                        .placeholder(R.drawable.ic_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.timeText.setText(finalDate);

        if(message_type.equals("text")) {

            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.get().load(c.getMessage())
                    .placeholder(R.drawable.ic_avatar).into(viewHolder.messageImage);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}


//    // Different types of rows
//    private static final int TYPE_ITEM_LEFT = 0;
//    private static final int TYPE_ITEM_RIGHT = 1;
//    private static final int TYPE_ITEM_DATE_CONTAINER = 2;
//
//public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    class ViewHolder0 extends RecyclerView.ViewHolder {
//        // Viewholder for row type 0
//    }
//
//    class ViewHolder1 extends RecyclerView.ViewHolder {
//        // Viewholder for row type 1
//    }
//
//    class ViewHolder2 extends RecyclerView.ViewHolder {
//        // Viewholder for row type 2
//    }
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
//        if (viewHolder.getItemViewType() == TYPE_ITEM_LEFT) {
//            // Code to populate type 0 view here
//
//        } else if (viewHolder.getItemViewType() == TYPE_ITEM_RIGHT) {
//            // Code to populate type 1 view here
//
//        } else if (viewHolder.getItemViewType() == TYPE_ITEM_DATE_CONTAINER) {
//            // Code to populate type 2 view here
//
//        }
//    }
