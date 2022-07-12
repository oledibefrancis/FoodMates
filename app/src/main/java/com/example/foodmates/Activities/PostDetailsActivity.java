package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    public static final String TAG = "PostDetailsActivity";
    Post post;
    ImageView  foodImage;
    TextView foodTitle;
    TextView carbs;
    TextView fat;
    TextView protein;
    TextView calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        foodImage = (ImageView)  findViewById(R.id.foodDetailImage);
        foodTitle = (TextView) findViewById(R.id.foodDetailTitle);
        carbs = (TextView) findViewById(R.id.carbsDetail);
        fat = (TextView) findViewById(R.id.fatDetail);
        protein = (TextView) findViewById(R.id.proteinDetail);
        calories = (TextView) findViewById(R.id.caloriesDetail);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        if(post.getImageUrl() instanceof String){
            Glide.with(this).load((post.getImageUrl())).into(foodImage);
        }
        else {
            Glide.with(this).load(post.getImage().getUrl()).into(foodImage);
        }

        foodTitle.setText(post.getKeyTitle());
//        carbs.setText(post.getCarbs());
//        fat.setText(post.getFat());
//        protein.setText(post.getProtein());
//        calories.setText(post.getCalories());

    }
}