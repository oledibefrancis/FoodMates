package com.example.foodmates.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Activities.FoodDetailsActivity;
import com.example.foodmates.Activities.PostDetailsActivity;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foods, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        ImageView foodImage;
        TextView foodTitle;
        Context context;
        TextView description;
        ImageView btnLike;
        ImageView btnLiked;
        ImageView btnSave;
        ImageView btnSaved;
        TextView createdAt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTitle = itemView.findViewById(R.id.foodTitles);
            foodImage = itemView.findViewById(R.id.foodImages);
            username = itemView.findViewById(R.id.postUser);
            this.context = itemView.getContext();
//            description = itemView.findViewById(R.id.description)
            btnLike = itemView.findViewById(R.id.btnLike);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnLiked = itemView.findViewById(R.id.btnLiked);
            btnSaved = itemView.findViewById(R.id.btnSaved);
            createdAt = itemView.findViewById(R.id.createdAt);

        }
        public void bind(Post post) {
            foodTitle.setText(post.getKeyTitle());
            if (post.getUser()!= null){
            username.setText(post.getUser().getUsername());
            createdAt.setText(post.calculateTimeAgo());
            }
            if(post.getImageUrl() instanceof String){
            Glide.with(context).load((post.getImageUrl())).into(foodImage);
            }
            else {
                Glide.with(context).load(post.getImage().getUrl()).into(foodImage);
            }

            btnLike.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        btnLiked.setVisibility(View.VISIBLE);
                        btnLike.setVisibility(View.INVISIBLE);

                        // In the backend, update the state of liked.
                        //
                        return super.onDoubleTap(e);
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return false;
                }
            });

            btnLiked.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        btnLike.setVisibility(View.VISIBLE);
                        btnLiked.setVisibility(View.INVISIBLE);
                        return super.onDoubleTap(e);
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return false;
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnSaved.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.INVISIBLE);
                    //a reference in the profile that stores object id of post the user saved
                    //when ever the click the save button you add the object to the list and when the unsave it removes the object from the list
                    //
                }
            });
            btnSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnSave.setVisibility(View.VISIBLE);
                    btnSaved.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
