package com.mazetireal.chat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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


public class MessageAdapter extends RecyclerView.Adapter {


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mCurrentUserId = mAuth.getCurrentUser().getUid();

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages c = mMessageList.get(position);

        if (mCurrentUserId.equals(c.getFrom())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_single_incoming, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_single_layout, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;

    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Messages c = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(c);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(c);
        }
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView displayName;
        public ImageView messageImage;
        public TextView timeText;

        SentMessageHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);
            timeText = view.findViewById(R.id.time_text_layout);
        }

        void bind(Messages message) {
            String from_user = message.getFrom();
            String message_type = message.getType();
            long message_time = message.getTime();
            Log.d("APPTAG", "Text: " + message_time);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String finalDate = outputFormat.format(message_time);

            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child("name").getValue().toString();

                    displayName.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            timeText.setText(finalDate);

            if (message_type.equals("text")) {

                messageText.setText(message.getMessage());
                messageImage.setVisibility(View.INVISIBLE);

            } else {

                messageText.setVisibility(View.INVISIBLE);
                Picasso.get().load(message.getMessage())
                        .placeholder(R.drawable.ic_avatar).into(messageImage);
            }

            messageText.setText(message.getMessage());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;
        public TextView timeText;

        ReceivedMessageHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);
            timeText = view.findViewById(R.id.time_text_layout);
        }

        void bind(Messages message) {
            String from_user = message.getFrom();
            String message_type = message.getType();
            long message_time = message.getTime();
            Log.d("APPTAG", "Text: " + message_time);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String finalDate = outputFormat.format(message_time);


            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("thumb_image").getValue().toString();

                    displayName.setText(name);

                    Picasso.get().load(image)
                            .placeholder(R.drawable.ic_avatar).into(profileImage);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            timeText.setText(finalDate);

            if (message_type.equals("text")) {

                messageText.setText(message.getMessage());
                messageImage.setVisibility(View.INVISIBLE);

            } else {

                messageText.setVisibility(View.INVISIBLE);
                Picasso.get().load(message.getMessage())
                        .placeholder(R.drawable.ic_avatar).into(messageImage);
            }
        }
    }

}