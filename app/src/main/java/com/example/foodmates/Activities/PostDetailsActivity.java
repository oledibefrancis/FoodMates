package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.foodmates.Models.Food;
import com.example.foodmates.Models.FoodDetail;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class PostDetailsActivity extends AppCompatActivity {

    public static final String API_URL = "https://api.spoonacular.com/recipes/%s/information?apiKey=8cf7ac7ac6f449e49a93e9cf5576c873";
    public static final String TAG = "PostDetailsActivity";
    Post post;
    FoodDetail result;
    TextView postDetail;
    ImageView foodImage;
    TextView foodTitle;
    TextView usernameDetail;
    TextView createdAtDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        foodImage = (ImageView) findViewById(R.id.foodDetailImage);
        foodTitle = (TextView) findViewById(R.id.foodDetailTitle);
        usernameDetail = (TextView) findViewById(R.id.usernameDetail);
        createdAtDetail = (TextView) findViewById(R.id.createdAtDetail);
        postDetail = (TextView) findViewById(R.id.postDetail);


        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        if (post.getImageUrl() instanceof String) {
            Glide.with(this).load((post.getImageUrl())).into(foodImage);
        } else {
            Glide.with(this).load(post.getImage().getUrl()).into(foodImage);
        }


        foodTitle.setText(post.getKeyTitle());
        postDetail.setText(post.getDetail());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i(TAG, "Post set: " + HtmlCompat.fromHtml(post.getDetail(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            postDetail.setText(HtmlCompat.fromHtml(post.getDetail(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
            postDetail.setText(Html.fromHtml(post.getDetail()));
        }
        if (post.getUser() != null) {
            usernameDetail.setText(post.getUser().getUsername());
            createdAtDetail.setText(post.calculateTimeAgo());
        }
        apiCall();


    }

    public void apiCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = String.format(API_URL,post.getId());

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess: " + json.toString());
                JSONObject jsonObject = json.jsonObject;
                try {
                    Log.i(TAG, "Results: " + jsonObject.toString());
                    result = FoodDetail.fromJsonObject(jsonObject);
                    saveInDataBase();
                } catch (JSONException e) {
                    Log.e("HomeFragment", "Error Storing api data: " + e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Unable to load api", throwable);
            }
        });

    }

    private void saveInDataBase() {
            post.setDetail(result.getDetail());
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                    }
                    else {
                        Log.e(TAG,"Failed to save api result to database");
                        Log.e(TAG,e.toString());
                    }
                }
            });
        }
    }
