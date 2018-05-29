package com.petclump.petclump.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.petclump.petclump.R;
import com.petclump.petclump.models.BaseMessage;
import com.petclump.petclump.models.MessagingDownloader;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.views.ChatRecycleViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
//This is made based on this tutorial!
// https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
public class ChattingActivity extends AppCompatActivity implements ProfileDownloader{
    private ChatRecycleViewAdapter chatRecycleViewAdapter;
    private RecyclerView recyclerView;
    private ArrayList<BaseMessage> baseMessageList;
    private Button chatview_send;
    private EditText chatview_editText;
    private LinearLayoutManager linearLayoutManager;
    private String name = "";
    private String my_id = "";
    private String friend_id = "";
    private String my_url = "";
    private String friend_url = "";
    private PetProfile pet = new PetProfile();
    private String TAG = "ChattingActivity";
    private MessagingDownloader downloader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        baseMessageList = new ArrayList<>();
        Intent intent = getIntent();
        name = intent.getExtras().getString("Name");
        my_id = intent.getStringExtra("my_id");
        friend_id = intent.getStringExtra("friend_id");


        BaseMessage message1 = new BaseMessage(1, "Hey What's up", "");
        BaseMessage message6 = new BaseMessage(2, "I'm a bot.", "");
//        BaseMessage message7 = new BaseMessage(2, "不要突然講中文啦", "10:10");
        baseMessageList.add(message1);
        baseMessageList.add(message6);
       // baseMessageList.add(message7);
        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        // setup photo
        downloader = new MessagingDownloader(this,my_id, friend_id,2);
        Log.d(TAG,"my:"+my_id+" other:"+friend_id);
        ChattingActivity t = this;
        pet.download(my_id, ()->{
            my_url = pet.getPhotoUrl(PetProfile.UrlKey.main);
            //Log.d(TAG,"my:"+my_url);
            pet.download(friend_id,()->{
                friend_url = pet.getPhotoUrl(PetProfile.UrlKey.main);
                setRecyclerView();


            });
        });
        downloader.downloadMore(baseMessageList, this);
        downloader.listenToRoom(baseMessageList, this);

        chatview_send = findViewById(R.id.button_chatview_send);
        chatview_editText = findViewById(R.id.chatview_editText);

        chatview_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pet.new_message(my_id,friend_id,chatview_editText.getText().toString(),()->{});
                chatview_editText.setText("");
            }
        });
        setActionBar(name);

    }
    public void setRecyclerView(){
        chatRecycleViewAdapter = new ChatRecycleViewAdapter(this, baseMessageList, my_url, friend_url);
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
/*        String tempMessage = chatview_editText.getText().toString();
        BaseMessage message1 = new BaseMessage(1, tempMessage, "me");
        BaseMessage message2 = new BaseMessage(2, "I'm a bot.", name);
        baseMessageList.add(message1);
        baseMessageList.add(message2);*/

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
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        TextView myText = findViewById(R.id.mytext);
        myText.setText(heading);
    }

    @Override
    public void didCompleteDownload() {
        chatRecycleViewAdapter.notifyDataSetChanged();
    }
}
