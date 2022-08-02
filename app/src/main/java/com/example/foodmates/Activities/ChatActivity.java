package com.example.foodmates.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.foodmates.Adapters.chatAdapter;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.Models.Message;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.example.foodmates.fragments.FoodHubFragment;
import com.example.foodmates.fragments.HomeFragment;
import com.parse.FindCallback;
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

import org.parceler.Parcels;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 100;
    static final String USER_GROUP_KEY = "userGroups";
    private static final String TAG = HomeFragment.class.getSimpleName();
    protected chatAdapter mAdapter;
    protected List<Message> mMessages;
    Chat chat;
    EditText etMessage;
    ImageButton ibSend;
    RecyclerView rvChat;
    Boolean mFirstLoad;
    Toolbar tbChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar(tbChat);
        setContentView(R.layout.activity_chat);
        tbChat = findViewById(R.id.tbChat);

        chat = (Chat) Parcels.unwrap(getIntent().getParcelableExtra(Chat.class.getSimpleName()));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.leaveGroup:
                leaveGroup(chat);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void leaveGroup(Chat chat) {
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation relation = user.getRelation(USER_GROUP_KEY);
        relation.remove(chat);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });
        Intent intent = new Intent(ChatActivity.this, FoodHubFragment.class);
        startActivity(intent);
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
                message.setUserIdPointer(ParseUser.getCurrentUser());
                message.setBody(data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            updateObject(message);
                        } else {
                            Log.e(TAG, "Failed to save message");
                        }
                    }
                });
                etMessage.setText(null);
            }
        });
    }


    private void updateObject(Message message) {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.getInBackground(chat.getObjectId(), (object, e) -> {
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
                    for (Chat c : chats) {
                        if (Objects.equals(c.getObjectId(), chat.getObjectId())) {
                            ParseRelation<Message> mRelation = c.getRelation("groupMessages");
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

