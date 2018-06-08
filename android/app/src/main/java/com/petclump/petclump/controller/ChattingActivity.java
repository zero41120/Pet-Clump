package com.petclump.petclump.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.R;
import com.petclump.petclump.models.BaseMessage;
import com.petclump.petclump.models.Cryptography.Cryptographer;
import com.petclump.petclump.models.Cryptography.KeyExchanger;
import com.petclump.petclump.models.MessagingDownloader;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.views.ChatRecycleViewAdapter;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

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
    // key
    private BigInteger my_public;
    private BigInteger friend_public;
    private BigInteger bigPRIME;
    private BigInteger priPRIME;
    private byte[] myShared;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        baseMessageList = new ArrayList<>();
        Intent intent = getIntent();
        name = intent.getExtras().getString("Name");
        my_id = intent.getStringExtra("my_id");
        friend_id = intent.getStringExtra("friend_id");

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.e(TAG,"User not logged in!");
            finish();
        }
        // download keys
        FirebaseFirestore.getInstance().collection("chats").document(PetProfile.getCombinedId(my_id, friend_id)).get().addOnCompleteListener(task->{
            Map<String, Object> ref = task.getResult().getData();
            my_public = new BigInteger(ref.get(my_id).toString());
            friend_public = new BigInteger(ref.get(friend_id).toString());
            bigPRIME = new BigInteger(ref.get("bigPrime").toString());
            priPRIME = new BigInteger(ref.get("priPrime").toString());
            Log.d(TAG,"my_public:"+my_public);
            Log.d(TAG,"friend_public:"+friend_public);
            Log.d(TAG,"bigPRIME:"+bigPRIME);
            Log.d(TAG,"priPRIME"+priPRIME);
            try {
                KeyExchanger my = new KeyExchanger(my_id, friend_public, bigPRIME, priPRIME);
                myShared = my.getSharedKey(friend_public);
                String sharedString = "";
                for(byte b : myShared){  sharedString += b; }
                Log.d(TAG, "mySharedKey: " + sharedString);
                setupUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
    private void setupUI(){
        // baseMessageList.add(message7);
        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        // setup photo
        downloader = new MessagingDownloader(this,my_id, friend_id, myShared, 2);
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

        chatview_send = findViewById(R.id.button_chatview_send);
        chatview_editText = findViewById(R.id.chatview_editText);

        chatview_send.setOnClickListener(v->{
            Cryptographer crypt = Cryptographer.getInstance();

            try {
                byte[] iv = crypt.generateInitializationVector();
                String iv_str = Cryptographer.convertIV(iv);
                String plaintext = chatview_editText.getText().toString();
                Log.d(TAG,"iv str:"+iv_str);
                String cipher = crypt.encrypt(myShared, iv, plaintext);
                pet.newMessage(my_id, friend_id, iv_str, cipher, Timestamp.now(), ()->{});
                BaseMessage temp = new BaseMessage(1, plaintext,Timestamp.now());
                baseMessageList.add(temp);
                messsageUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        setActionBar(name);
    }
    public void setRecyclerView(){
        chatRecycleViewAdapter = new ChatRecycleViewAdapter(this, baseMessageList, my_url, friend_url, friend_id, my_id);
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
        recyclerView.post(()-> {
                recyclerView.scrollToPosition(chatRecycleViewAdapter.getItemCount()-1>= 0 ? chatRecycleViewAdapter.getItemCount()-1: 0);
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
        actionBar.setDisplayHomeAsUpEnabled(true);
        myText.setText(heading);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
        }

        return true;
    }

    @Override
    public void didCompleteDownload() {
        if (baseMessageList.size()!=0){
            //Integer lastSize = baseMessageList.size();
            Log.d(TAG, String.valueOf(baseMessageList.size()));
            setRecyclerView();
            //chatRecycleViewAdapter.notifyDataSetChanged();
        }else{
            setRecyclerView();
        }
    }
}
