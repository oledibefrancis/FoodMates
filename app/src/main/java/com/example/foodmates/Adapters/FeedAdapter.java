package com.example.foodmates.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Activities.FoodDetailsActivity;
import com.example.foodmates.Activities.PostDetailsActivity;
import com.example.foodmates.FeedItem;
import com.example.foodmates.Models.Food;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    public static final String TAG = "FeedAdapter";
    Context context;
    List<FeedItem> posts;


    public FeedAdapter(Context context, List<FeedItem> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foods, parent, false);
        return new FeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem post = posts.get(position);
        holder.bind(post);
        if (post instanceof Food) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FoodDetailsActivity.class);
                    intent.putExtra(Food.class.getSimpleName(), Parcels.wrap((Food) post));
                    context.startActivity(intent);
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to delete this post?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteObject(((Post) post).getObjectId());
                                    posts.remove(position);
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("No", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<FeedItem> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public void deleteObject(String objectId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                object.deleteInBackground(e2 -> {
                    if (e2 == null) {
                        Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error: " + e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView posted;
        ImageView foodImage;
        TextView foodTitle;
        Context context;
        ImageView btnLike;
        ImageView btnLiked;
        ImageView btnSave;
        ImageView btnSaved;
        TextView createdAt;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTitle = itemView.findViewById(R.id.foodTitles);
            foodImage = itemView.findViewById(R.id.foodImages);
            username = itemView.findViewById(R.id.postUser);
            this.context = itemView.getContext();
            posted = itemView.findViewById(R.id.posted);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnLiked = itemView.findViewById(R.id.btnLiked);
            btnSaved = itemView.findViewById(R.id.btnSaved);
            createdAt = itemView.findViewById(R.id.createdAt);
            card = itemView.findViewById(R.id.card);

        }

        public void bind(FeedItem post) {

            foodTitle.setText(post.getTitle());
            if (post instanceof Food) {
                foodTitle.setText(((Food) post).getTitle());
                String imageUrl;
                imageUrl = post.getImageUrl();
                Glide.with(context).load((imageUrl)).into(foodImage);

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
                    }
                });
                btnSaved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnSave.setVisibility(View.VISIBLE);
                        btnSaved.setVisibility(View.INVISIBLE);
                    }
                });
            } else {

                if (((Post) post).getUser() != null) {
                    Log.e(TAG, ((Post) post).getUser().getUsername());
                    username.setText(((Post) post).getUser().getUsername());
                    createdAt.setText(((Post) post).calculateTimeAgo());
                    card.setCardBackgroundColor(Color.parseColor("#C1EAB4"));
                    posted.setVisibility(View.VISIBLE);
                } else {
                    username.setText("");
                    createdAt.setText("");
                    card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    posted.setVisibility(View.INVISIBLE);
                }
                if (post.getImageUrl() instanceof String) {
                    Glide.with(context)
                            .load((post.getImageUrl()))
                            .into(foodImage);
                } else {
                    Glide.with(context)
                            .load(((Post) post).getImage().getUrl())
                            .into(foodImage);
                }

                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<Post> relation = user.getRelation("likes");
                ParseRelation<Post> savedRelation = user.getRelation("savedPost");
                ParseQuery<Post> postQuery = relation.getQuery();
                ParseQuery<Post> savedPostQuery = savedRelation.getQuery();

                postQuery.findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> liked, ParseException e) {
                        for (Post p : liked) {
                            if (Objects.equals(p.getObjectId(), ((Post) post).getObjectId())) {
                                btnLiked.setVisibility(View.VISIBLE);
                                btnLike.setVisibility(View.INVISIBLE);
                                return;
                            } else {
                                btnLiked.setVisibility(View.INVISIBLE);
                                btnLike.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

                savedPostQuery.findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> saved, ParseException e) {
                        for (Post s : saved) {
                            if (Objects.equals(s.getObjectId(), ((Post) post).getObjectId())) {
                                btnSaved.setVisibility(View.VISIBLE);
                                btnSave.setVisibility(View.INVISIBLE);
                                return;
                            } else {
                                btnSaved.setVisibility(View.INVISIBLE);
                                btnSave.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });


                btnLike.setOnTouchListener(new View.OnTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            btnLiked.setVisibility(View.VISIBLE);
                            btnLike.setVisibility(View.INVISIBLE);
                            relation.add(((Post) post));
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                }
                            });
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
                            relation.remove(((Post) post));
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                }
                            });

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
                        savedRelation.add(((Post) post));
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                            }
                        });
                    }
                });
                btnSaved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnSave.setVisibility(View.VISIBLE);
                        btnSaved.setVisibility(View.INVISIBLE);
                        savedRelation.remove(((Post) post));
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                            }
                        });
                    }
                });

            }

        }
    }

}
