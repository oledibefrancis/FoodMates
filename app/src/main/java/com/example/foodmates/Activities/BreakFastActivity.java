package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.foodmates.Adapters.chatAdapter;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.Models.Message;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.example.foodmates.fragments.HomeFragment;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class BreakFastActivity extends AppCompatActivity {

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 100;
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    private static final String TAG = "BreakFastActivity";
    protected chatAdapter mAdapter;
    protected List<Message> mMessages;
    EditText etMessage;
    ImageButton ibSend;
    RecyclerView rvChat;
    Boolean mFirstLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_fast);


        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser();
        } else {
            login();
        }

        refreshMessages();

        String websocketUrl = "wss://codepathparsechatlab.b4a.io/"; //

        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);

        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
            mMessages.add(0, object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    rvChat.scrollToPosition(0);
                }
            });
        });

    }

    private void startWithCurrentUser() {
        setupMessagePosting();
    }

    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        ibSend = (ImageButton) findViewById(R.id.ibSend);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new chatAdapter(this, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setUserId(ParseUser.getCurrentUser().getObjectId());
                message.setBody(data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            updateObject(message);
                        } else {
                            Log.e(TAG, "Falied to save message");
                        }
                    }
                });
                etMessage.setText(null);
            }
        });
    }

    private void updateObject(Message message) {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.getInBackground("1GUf8QzfZ5", (object, e) -> {
            if (e == null) {
                ParseRelation<Message> relation = object.getRelation("groupMessages");
                relation.add(message);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        refreshMessages();
                    }
                });

            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.toString());
            }
        });
    }

    void refreshMessages() {

        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.findInBackground(new FindCallback<Chat>() {
            @Override
            public void done(List<Chat> chats, ParseException e) {
                if (e == null) {
                    ParseRelation<Message> mRelation = chats.get(0).getRelation("groupMessages");
                    ParseQuery<Message> mQuery = mRelation.getQuery();
                    mQuery.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
                    mQuery.orderByDescending("createdAt");
                    mQuery.findInBackground(new FindCallback<Message>() {
                        @Override
                        public void done(List<Message> groupMessage, ParseException e) {
                            if (e == null) {
                                mMessages.clear();
                                mMessages.addAll(groupMessage);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Log.e(TAG, "Error Loading Messages" + e);
                            }
                        }
                    });
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    } else {
                        Log.e(TAG, "Error Loading Messages" + e);
                    }
                }
            }
        });
    }


    public void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous Login Failed:", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }


}