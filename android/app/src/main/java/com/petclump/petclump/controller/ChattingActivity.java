package com.petclump.petclump.controller;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.petclump.petclump.R;
import com.petclump.petclump.models.BaseMessage;
import com.petclump.petclump.views.ChatRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;
//This is made based on this tutorial!
// https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
public class ChattingActivity extends AppCompatActivity {
    private ChatRecycleViewAdapter chatRecycleViewAdapter;
    private RecyclerView recyclerView;
    private List<BaseMessage> baseMessageList;
    private Button chatview_send;
    private EditText chatview_editText;
    private LinearLayoutManager linearLayoutManager;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        baseMessageList = new ArrayList<>();
        Intent intent = getIntent();
        name = intent.getExtras().getString("Name");
        setActionBar(name);
        BaseMessage message1 = new BaseMessage(1, "Hey What's up", "me");
//        BaseMessage message2 = new BaseMessage(2, "Not much and you?", "10:09");
//        BaseMessage message3 = new BaseMessage(1, "I'm just about to eat", "10:09");
//        BaseMessage message4 = new BaseMessage(2, "What are you gonna eat?", "10:09");
//        BaseMessage message5 = new BaseMessage(2, "對了", "10:09");
        BaseMessage message6 = new BaseMessage(2, "米耳耳朵都是米^_^", name);
//        BaseMessage message7 = new BaseMessage(2, "不要突然講中文啦", "10:10");
        baseMessageList.add(message1);
//        baseMessageList.add(message2);
//        baseMessageList.add(message3);
//        baseMessageList.add(message4);
//        baseMessageList.add(message5);
        baseMessageList.add(message6);
       // baseMessageList.add(message7);
        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        setRecyclerView();
        chatview_send = findViewById(R.id.button_chatview_send);
        chatview_editText = findViewById(R.id.chatview_editText);
        chatview_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messsageUpdate();
            }
        });

    }
    public void setRecyclerView(){
        chatRecycleViewAdapter = new ChatRecycleViewAdapter(this, baseMessageList);
        linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatRecycleViewAdapter);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                recyclerView.smoothScrollToPosition(chatRecycleViewAdapter.getItemCount()-1);
            }
        });
    }
    public void messsageUpdate(){
        String tempMessage = chatview_editText.getText().toString();
        BaseMessage message1 = new BaseMessage(1, tempMessage, "me");
        BaseMessage message2 = new BaseMessage(2, "耳多都是米", name);
        baseMessageList.add(message1);
        baseMessageList.add(message2);
        chatview_editText.getText().clear();
        setRecyclerView();
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(
                       INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);


    }

    public void setActionBar(String heading) {
        // TODO Auto-generated method stub

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        actionBar.setTitle(heading);
        actionBar.show();

    }

}
