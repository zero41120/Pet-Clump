package com.petclump.petclump.controller;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        baseMessageList = new ArrayList<>();
        BaseMessage message1 = new BaseMessage(1, "Hey What's up", "10:09");
        BaseMessage message2 = new BaseMessage(2, "Not much and you?", "10:09");
        BaseMessage message3 = new BaseMessage(1, "I'm just about to eat", "10:09");
        baseMessageList.add(message1);
        baseMessageList.add(message2);
        baseMessageList.add(message3);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatRecycleViewAdapter);
    }
    public void messsageUpdate(){
        String tempMessage = chatview_editText.getText().toString();
        BaseMessage message = new BaseMessage(1, tempMessage, "3:05");
        baseMessageList.add(message);
        chatview_editText.getText().clear();
        setRecyclerView();
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(
                       INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);

    }

}
