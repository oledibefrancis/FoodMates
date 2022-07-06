package com.example.foodmates.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.foodmates.Models.Message;
import com.example.foodmates.R;
import com.example.foodmates.Adapters.chatAdapter;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {


    private static final String TAG = HomeFragment.class.getSimpleName();
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 100;
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    EditText etMessage;
    ImageButton ibSend;
    RecyclerView rvChat;
    protected chatAdapter mAdapter;
    protected List<Message> mMessages;
    Boolean mFirstLoad;

    public ChatFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (ParseUser.getCurrentUser() != null){
            startWithCurrentUser();
        }
        else {
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

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
            mMessages.add(0, object);
        // RecyclerView updates need to be run on the UI thread
            getActivity().runOnUiThread(new Runnable() {
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
        etMessage = (EditText) getView().findViewById(R.id.etMessage);
        ibSend = (ImageButton) getView().findViewById(R.id.ibSend);
        rvChat = (RecyclerView) getView().findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new chatAdapter(getContext(), userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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
                        if(e == null){
                            Toast.makeText(getActivity(), "Succesfully created message on Parse", Toast.LENGTH_SHORT).show();
                            refreshMessages();
                        }
                        else {
                           Log.e(TAG,"Falied to save message");
                        }
                    }
                });
                etMessage.setText(null);
            }
        });
    }
    void refreshMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if(e == null){
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                    if(mFirstLoad){
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                    else {
                        Log.e(TAG,"Error Loading Messages"+ e);
                    }
                }
            }
        });
    }

    private void login(){
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Anonymous Login Failed:", e);
                }
                else {
                    startWithCurrentUser();
                }
            }
        });
    }

}