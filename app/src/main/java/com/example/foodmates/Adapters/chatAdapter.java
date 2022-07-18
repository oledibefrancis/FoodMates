package com.example.foodmates.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.Models.Message;
import com.example.foodmates.R;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MessageViewHolder>{
    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;

    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    @Override
    public int getItemViewType(int position) {
        if (isUser(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isUser(int position) {
        Message message = mMessages.get(position);
        return message.getUserId() != null && message.getUserId().equals(mUserId);
    }
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
    public chatAdapter(Context context, String userId, List<Message> messages) {
        this.mUserId = userId;
        mMessages = messages;
        mContext = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder{

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageOther;
        TextView body;
        TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            body = (TextView)itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) {
            Glide.with(mContext)
                    .load(getProfileUrl(message.getUserId()))
                    .circleCrop()
                    .into(imageOther);
            body.setText(message.getBody());
            name.setText(message.getUserId());
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageMe;
        TextView body;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
        }

        @Override
        public void bindMessage(Message message) {
            Glide.with(mContext)
                    .load(getProfileUrl(message.getUserId()))
                    .circleCrop()
                    .into(imageMe);
            body.setText(message.getBody());        }
    }

}
