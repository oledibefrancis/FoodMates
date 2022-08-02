package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    TextView postedDetail;
    CardView cardDetail;
    ImageView btnSavedDetail;
    ImageView btnSaveDetail;
    ImageView btnLikeDetail;
    ImageView btnLikedDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        foodImage = findViewById(R.id.foodDetailImage);
        foodTitle = findViewById(R.id.foodDetailTitle);
        usernameDetail = findViewById(R.id.usernameDetail);
        createdAtDetail = findViewById(R.id.createdAtDetail);
        postDetail = findViewById(R.id.postDetail);
        postedDetail = findViewById(R.id.postedDetail);
        cardDetail = findViewById(R.id.cardDetail);
        btnSavedDetail = findViewById(R.id.btnSavedDetail);
        btnSaveDetail = findViewById(R.id.btnSaveDetail);
        btnLikeDetail = findViewById(R.id.btnLikeDetail);
        btnLikedDetail = findViewById(R.id.btnLikedDetail);


        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        if (post.getImageUrl() instanceof String) {
            Glide.with(this).load((post.getImageUrl())).into(foodImage);
        } else {
            Glide.with(this).load(post.getImage().getUrl()).into(foodImage);
        }
        apiCall();

        foodTitle.setText(post.getTitle());
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
            cardDetail.setCardBackgroundColor(Color.parseColor("#C1EAB4"));
            postedDetail.setVisibility(View.VISIBLE);
        } else {
            usernameDetail.setText("");
            createdAtDetail.setText("");
            cardDetail.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            postedDetail.setVisibility(View.INVISIBLE);

            ParseUser user = ParseUser.getCurrentUser();
            ParseRelation<Post> relation = user.getRelation("likes");
            ParseRelation<Post> savedRelation = user.getRelation("savedPost");
            ParseQuery<Post> postQuery = relation.getQuery();
            ParseQuery<Post> savedPostQuery = savedRelation.getQuery();


            postQuery.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> liked, ParseException e) {
                    for (Post p : liked) {
                        if (Objects.equals(p.getObjectId(), post.getObjectId())) {
                            btnLikedDetail.setVisibility(View.VISIBLE);
                            btnLikeDetail.setVisibility(View.INVISIBLE);
                            return;
                        } else {
                            btnLikedDetail.setVisibility(View.INVISIBLE);
                            btnLikeDetail.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            savedPostQuery.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> saved, ParseException e) {
                    for (Post s : saved) {
                        if (Objects.equals(s.getObjectId(), post.getObjectId())) {
                            btnSavedDetail.setVisibility(View.VISIBLE);
                            btnSaveDetail.setVisibility(View.INVISIBLE);
                            return;
                        } else {
                            btnSavedDetail.setVisibility(View.INVISIBLE);
                            btnSaveDetail.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

        }

    }

    public void apiCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = String.format(API_URL, post.getId());

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
                if (e == null) {
                } else {
                    Log.e(TAG, "Failed to save api result to database");
                    Log.e(TAG, e.toString());
                }
            }
        });
    }
}

