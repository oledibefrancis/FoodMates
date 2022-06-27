package com.example.careermatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    Context context;
    List<Food> foods;

    public FoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
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
        return foods.size();
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
}
