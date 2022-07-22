package com.example.foodmates.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Activities.FoodDetailsActivity;
import com.example.foodmates.Models.Food;
import com.example.foodmates.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements Filterable {
    Context context;
    List<Food> foods;
    List<Food> feedsToShow;


    private Filter filterExample = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Food> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(foods);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Food food : foods) {
                    if (food.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(food);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            feedsToShow.clear();
            if (results.values == null) {
                Log.e("PostsAdapter", "results.values is null");
            } else {
                feedsToShow.addAll((List) results.values);
                Log.d("PostsAdapter", "Displaying " + results.count + " results through filter");
            }
            notifyDataSetChanged();
        }
    };

    public FoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
        feedsToShow = new ArrayList<>(foods);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View foodView = LayoutInflater.from(context).inflate(R.layout.item_foods, parent, false);
        return new ViewHolder(foodView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.bind(food);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailsActivity.class);
                intent.putExtra(Food.class.getSimpleName(), Parcels.wrap(food));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return feedsToShow.size();
    }

    @Override
    public Filter getFilter() {
        return filterExample;
    }

    public void clear() {
        foods.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Food> list) {
        foods.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodTitle;
        ImageView foodImage;
        ImageView btnLike;
        ImageView btnLiked;
        ImageView btnSave;
        ImageView btnSaved;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTitle = itemView.findViewById(R.id.foodTitles);
            foodImage = itemView.findViewById(R.id.foodImages);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnLiked = itemView.findViewById(R.id.btnLiked);
            btnSaved = itemView.findViewById(R.id.btnSaved);

        }

        public void bind(Food food) {
            foodTitle.setText(food.getTitle());
            String imageUrl;
            imageUrl = food.getImageUrl();
            Glide.with(context).load((imageUrl)).into(foodImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FoodDetailsActivity.class);
                    intent.putExtra(Food.class.getSimpleName(), Parcels.wrap(food));
                    context.startActivity(intent);
                }
            });

            btnLike.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        btnLiked.setVisibility(View.VISIBLE);
                        btnLike.setVisibility(View.INVISIBLE);

                        //TODO 1) In the backend, update the state of liked.
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
