package com.petclump.petclump.controller;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.GPS.MyService;
import com.petclump.petclump.models.OwnerProfile;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.views.Popup;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity implements ProfileDownloader{
    private static final String TAG = "User Info Activity";
    private ImageButton button_add_pet, button_why;
    private CircularImageView profile_pet1, profile_pet2, profile_pet3;
    private TextView name_label, gender_label, birthday_label, range_label;
    private Button edit_button;
    private Context c;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;
    private OwnerProfile profile = OwnerProfile.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(user == null){
            Log.d(TAG, "onCreate: User not logged in");
            finish();
        }
        setupUI();
        setActionBar(String.valueOf(getText(R.string.About_me)));
    }

    private void setupUI(){
        setContentView(R.layout.activity_user_info);
        c = getApplicationContext();
        constraintSet    = new ConstraintSet();
        constraintLayout = findViewById(R.id.user_info_layout);
        constraintSet.clone(constraintLayout);

        profile_pet1 = findViewById(R.id.profile_pet1);
        profile_pet2 = findViewById(R.id.profile_pet2);
        profile_pet3 = findViewById(R.id.profile_pet3);

        name_label      = findViewById(R.id.name_label);
        gender_label    = findViewById(R.id.gender_label);
        birthday_label  = findViewById(R.id.birthday_label);
        range_label     = findViewById(R.id.user_match_value);

        //edit_button     = findViewById(R.id.edit_button);
        button_why = findViewById(R.id.button_why);
        // initialize primary pet images
        initializePrimaryPet();

        button_why.setOnClickListener(v ->
            startActivity(new Intent(c, Popup.class))
        );
        // Enter Pet_info to create new pet


        Intent i = new Intent(c, PetInfoActivity.class);
        profile_pet1.setOnClickListener(v->{
            i.putExtra("sequence", 0);
            startActivity(i);
        });
        profile_pet2.setOnClickListener(v->{
            i.putExtra("sequence", 1);
            startActivity(i);
        });
        profile_pet3.setOnClickListener(v->{
            i.putExtra("sequence", 2);
            startActivity(i);
        });

        profile.download(user.getUid(), this);

    }

    @Override
    public void didCompleteDownload() {
        if(null == profile){
            Log.d(TAG,"_update without setting up profile");
            return;
        }
        name_label.setText(profile.getName());
        gender_label.setText(profile.getGender());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime((Date) profile.getBirthday());
        String t = String.valueOf(OwnerProfile.num_month(calendar.get(Calendar.MONTH)+1))+" "
                +String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+" "
                +String.valueOf(calendar.get(Calendar.YEAR));
        birthday_label.setText(t);
        Integer gdp = profile.getDistancePerference();
        if (gdp < 1000){
            range_label.setText(String.valueOf("Within "+ profile.getDistancePerference())+ " km");
        } else{
            range_label.setText("No preference");
        }

    }
    private void initializePrimaryPet(){
        PetProfile thePet = new PetProfile();

        //profile_pet1
        thePet.download(user.getUid()+0,()->{
            String url = thePet.getUrl("main_profile_url");
            new DownloadImageTask(profile_pet1, this).execute(url);
            //Toast.makeText(this, "1 set up"+url, Toast.LENGTH_SHORT).show();
        });
        //profile_pet2
        thePet.download(user.getUid()+1,()->{
            String url = thePet.getUrl("main_profile_url");
            new DownloadImageTask(profile_pet2, this).execute(url);
            //Toast.makeText(this, "2 set up", Toast.LENGTH_SHORT).show();
        });
        //profile_pet3
        thePet.download(user.getUid()+2,()->{
            String url = thePet.getUrl("main_profile_url");
            new DownloadImageTask(profile_pet3, this).execute(url);
            //Toast.makeText(this, "3 set up", Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);

    }
    public void setActionBar(String heading) {
        // TODO Auto-generated method stub

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView myText = findViewById(R.id.mytext);
        myText.setText(heading);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(c, UserInfoEditActivity.class);
                startActivity(i);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

}

