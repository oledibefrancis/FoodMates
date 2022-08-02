package com.example.foodmates.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Activities.ChatActivity;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

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
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ImageView groupImage;
        Button joinGroup;
        Button openGroup;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupImage = itemView.findViewById(R.id.groupImage);
            joinGroup = itemView.findViewById(R.id.joinGroup);
            openGroup = itemView.findViewById(R.id.openGroup);
        }

        public void bind(Chat chat) {
            groupName.setText(chat.getGroupName());
            Glide.with(context)
                    .load(chat.getImage().getUrl())
                    .into(groupImage);

            ParseUser user = ParseUser.getCurrentUser();
            ParseRelation<Chat > relation = user.getRelation("userGroups");
            ParseQuery<Chat> query = relation.getQuery();
            query.findInBackground(new FindCallback<Chat>() {
                @Override
                public void done(List<Chat> groups, ParseException e) {
                    for(Chat g : groups){
                        if (Objects.equals(g.getObjectId(), chat.getObjectId())){
                            openGroup.setVisibility(View.VISIBLE);
                            joinGroup.setVisibility(View.INVISIBLE);
                            return;
                        }
                        else {
                            joinGroup.setVisibility(View.VISIBLE);
                            openGroup.setVisibility(View.INVISIBLE);
                        }
                    }
                    chats.addAll(groups);

                }
            });

            joinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(Chat.class.getSimpleName(), Parcels.wrap(chat));
                    relation.add(chat);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        }
                    });
                    context.startActivity(intent);
                }
            });

            openGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(Chat.class.getSimpleName(), Parcels.wrap(chat));
                    context.startActivity(intent);
                }
            });
        }
    }
}
