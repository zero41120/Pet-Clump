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

import com.google.firebase.Timestamp;
import com.petclump.petclump.R;
import com.petclump.petclump.models.BaseMessage;
import com.petclump.petclump.models.MessagingDownloader;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.views.ChatRecycleViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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


       // baseMessageList.add(message7);
        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        // setup photo
        downloader = new MessagingDownloader(this,my_id, friend_id,2);
        Log.d(TAG,"my:"+my_id+" other:"+friend_id);
        ChattingActivity theCTX = this;
        //setRecyclerView();
        pet.download(my_id, ()->{
            my_url = pet.getPhotoUrl(PetProfile.UrlKey.main);
            //Log.d(TAG,"my:"+my_url);
            pet.download(friend_id,()->{
                friend_url = pet.getPhotoUrl(PetProfile.UrlKey.main);
                downloader.listenToRoom(baseMessageList, theCTX);
                setRecyclerView();


            });
        });
       /*downloader.downloadMore(baseMessageList, ()->{
            setRecyclerView();

        });*/
        chatview_send = findViewById(R.id.button_chatview_send);
        chatview_editText = findViewById(R.id.chatview_editText);
        Calendar calendar = new GregorianCalendar();
        chatview_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chatview_editText.getText().toString();

                pet.new_message(my_id,friend_id,text, Timestamp.now(),()->{});
                BaseMessage temp = new BaseMessage(1, text,Timestamp.now());
                baseMessageList.add(temp);
                messsageUpdate();
            }
        });
        setActionBar(name);

    }
    public void setRecyclerView(){
        chatRecycleViewAdapter = new ChatRecycleViewAdapter(this, baseMessageList, my_url, friend_url);
        chatRecycleViewAdapter.setHasStableIds(true);
        linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatRecycleViewAdapter);
        Log.d(TAG, "Item count" + String.valueOf(chatRecycleViewAdapter.getItemCount()));
        recyclerView.scrollToPosition(chatRecycleViewAdapter.getItemCount()-1>= 0 ? chatRecycleViewAdapter.getItemCount()-1: 0);



    }
    public void messsageUpdate(){
        //chatRecycleViewAdapter.notifyDataSetChanged();
        chatview_editText.getText().clear();
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                recyclerView.scrollToPosition(chatRecycleViewAdapter.getItemCount()-1>= 0 ? chatRecycleViewAdapter.getItemCount()-1: 0);
            }
        });
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
//        setRecyclerView();
        if (chatRecycleViewAdapter!=null){
            //Integer lastSize = baseMessageList.size();
            Log.d(TAG, String.valueOf(baseMessageList.size()));
            chatRecycleViewAdapter.notifyDataSetChanged();
        }else{
            setRecyclerView();

        }



    }
}
