package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Models.Food;
import com.example.foodmates.R;

import org.parceler.Parcels;

public class FoodDetailsActivity extends AppCompatActivity {

    public static final String TAG = "FoodDetailsActivity";
    Food food;
    ImageView foodImage;
    TextView foodTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        foodImage = (ImageView) findViewById(R.id.foodImage);
        foodTitle = (TextView) findViewById(R.id.foodTitle);


        food = (Food) Parcels.unwrap(getIntent().getParcelableExtra(Food.class.getSimpleName()));

        Glide.with(this).load((food.imageurl)).into(foodImage);
        foodTitle.setText(food.getTitle());

    }
}