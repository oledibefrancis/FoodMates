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
import com.example.foodmates.Models.UserPost;
import com.example.foodmates.R;
import com.parse.ParseFile;

import java.util.List;



public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder>{
    Context context;
    List<UserPost> posts;


    public UserPostAdapter(Context context, List<UserPost> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_post, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserPost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView username;
        ImageView foodImage;
        TextView foodTitle;
        Context context;
        TextView createdAt;



        public  ViewHolder(@NonNull View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodTitle = itemView.findViewById(R.id.foodTitle);
            this.context = itemView.getContext();
            createdAt = itemView.findViewById(R.id.createdAt);
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);

        }

        public void bind(UserPost post) {

            username.setText(post.getUser().getUsername());
            foodTitle.setText(post.getDescription());
            post.getCreatedAt();
//            tvCreatedAt.setText(post.calculateTimeAgo());

            ParseFile image = post.getImage();
            if (image != null){
                Glide.with(context).load(image.getUrl()).into(foodImage);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }


    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<UserPost> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}