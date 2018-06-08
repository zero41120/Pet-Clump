package com.petclump.petclump.controller;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.PetProfile;

/*****
 * * *
 * * *
 * * *
 *****/
public class MatchingActivity extends AppCompatActivity {
    private Button vm_button_settings;
    private CircularImageView match_pet1, match_pet2, match_pet3;
    private TextView match_name1, match_name2, match_name3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        //vm_button_settings = findViewById(R.id.vm_button_settings);
        match_pet1 = findViewById(R.id.match_pet1);
        match_pet2 = findViewById(R.id.match_pet3);
        match_pet3 = findViewById(R.id.match_pet2);
        match_name1 = findViewById(R.id.match_name1);
        match_name2 = findViewById(R.id.match_name2);
        match_name3 = findViewById(R.id.match_name3);
//        vm_button_settings.setOnClickListener(v -> {
//            startActivity(new Intent(this, UserInfoActivity.class));
//        });
        setActionBar(String.valueOf(getText(R.string.View_Match_As)));
    }
    @Override
    protected void onResume() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        super.onResume();
        Intent i = new Intent(this, MatchingTab.class);
        match_pet1.setOnClickListener(v->{
            i.putExtra("petId", uid + "0");
            startActivity(i);
        });
        match_pet2.setOnClickListener(v->{
            i.putExtra("petId", uid + "1");
            startActivity(i);
        });
        match_pet3.setOnClickListener(v->{
            i.putExtra("petId", uid + "2");
            startActivity(i);
        });

        initializePrimaryPet();
    }
    private void initializePrimaryPet(){
        PetProfile thePet = new PetProfile();
        FirebaseAuth user = FirebaseAuth.getInstance();
        //profile_pet1
        thePet.download(user.getUid()+0,()->{
            String url = thePet.getUrl("main_profile_url");
            String name = thePet.getName();
            new DownloadImageTask(match_pet1, this).execute(url);
            match_name1.setText(name);
            //Toast.makeText(this, "1 set up", Toast.LENGTH_SHORT).show();
        });
        //profile_pet2
        thePet.download(user.getUid()+1,()->{
            String url = thePet.getUrl("main_profile_url");
            new DownloadImageTask(match_pet2, this).execute(url);
            String name = thePet.getName();
            match_name2.setText(name);
            //Toast.makeText(this, "2 set up", Toast.LENGTH_SHORT).show();
        });
        //profile_pet3
        thePet.download(user.getUid()+2,()->{
            String url = thePet.getUrl("main_profile_url");
            String name = thePet.getName();
            new DownloadImageTask(match_pet3, this).execute(url);
            match_name3.setText(name);
            //Toast.makeText(this, "3 set up", Toast.LENGTH_SHORT).show();
        });
    }
    // This method will just show the menu item (which is our button "ADD")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /* Here is the event handler for the menu button that I forgot in class.
    The value returned by item.getItemID() is
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                /*the R.id.action_favorite is the ID of our button (defined in strings.xml).
                Change Activity here (if that's what you're intending to do, which is probably is).
                 */
                Intent i = new Intent(this, UserInfoActivity.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
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
}
