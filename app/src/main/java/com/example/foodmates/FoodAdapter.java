package com.example.foodmates;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements Filterable {
    Context context;
    List<Food> foods;
    List<Food> feedsToShow;

    public FoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
        feedsToShow = new ArrayList<>(foods);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View foodView = LayoutInflater.from(context).inflate(R.layout.item_foods,parent,false);
        return new ViewHolder(foodView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food  = foods.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return feedsToShow.size();
    }

    @Override
    public Filter getFilter() {
        return fliterexample;
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodTitle;
        ImageView foodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTitle = itemView.findViewById(R.id.foodTitle);
            foodImage = itemView.findViewById(R.id.foodImage);

    }

        public void bind(Food food) {
            foodTitle.setText(food.getTitle());
            String imageUrl;
            imageUrl = food.getImageurl();
            Glide.with(context).load((imageUrl)).into(foodImage);
        }
    }


    private Filter fliterexample = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Food> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                //If the user does not enter anything, display the whole list
                filteredList.addAll(foods);
            }
            else{
                //toLowercase makes it so that the search is not case sensitive
                //trim takes away extra whitespaces
                String filterPattern = constraint.toString().toLowerCase().trim();

                //iterate to see which post matched filterPattern
                for(Food food: foods){
                    if(food.getTitle().toLowerCase().contains(filterPattern)) {
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


}
