package com.example.foodmates.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Activities.ChatActivity;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.R;

import org.parceler.Parcels;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    Context context;
    List<Chat> chats;

    public GroupChatAdapter(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groupView = LayoutInflater.from(context).inflate(R.layout.item_group_chat, parent, false);
        return new ViewHolder(groupView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.bind(chat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Chat.class.getSimpleName(), Parcels.wrap(chat));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ImageView groupImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupImage = itemView.findViewById(R.id.groupImage);
        }

        public void bind(Chat chat) {
            groupName.setText(chat.getGroupName());
            Glide.with(context)
                    .load(chat.getImage().getUrl())
                    .into(groupImage);
        }
    }
}
