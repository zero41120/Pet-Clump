package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.petclump.petclump.models.OwnerProfile;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "User Info Activity";
    private int number_of_pets = 1;
    private ImageButton button_add_pet, button_why;
    private CircularImageView profile_pet1, profile_pet2, profile_pet3;
    private TextView name_pet1, name_pet2, name_pet3;
    private TextView name_label, gender_label, birthday_label, range_label;
    private Button edit_button;
    private Context c;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;
    private Date birthday;
    private OwnerProfile profile;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(user == null){
            Log.d(TAG, "onCreate: User not logged in");
            finish();
        }
        setupUI();
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

        edit_button     = findViewById(R.id.edit_button);

        button_why = findViewById(R.id.button_why);
        button_why.setOnClickListener(v ->
            startActivity(new Intent(c, Popup.class))
        );
        // Enter Pet_info to create new pet
        edit_button.setOnClickListener(v-> startActivity(new Intent(c, UserInfoEditActivity.class)));

        profile = new OwnerProfile();
        profile.download(user.getUid(), ()->{
                if(null == profile){
                Log.d(TAG,"_update without setting up profile");
            }
            name_label.setText(profile.getName());
            gender_label.setText(profile.getGender());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime((Date) profile.getBirthday());
            String t = String.valueOf(OwnerProfile.num_month(calendar.get(Calendar.MONTH)+1))+" "
                    +String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+" "
                    +String.valueOf(calendar.get(Calendar.YEAR));
            birthday_label.setText(t);
            range_label.setText(String.valueOf(profile.getDistancePerference()));
        });

        Intent i = new Intent(c, PetInfoActivity.class);
        profile_pet1.setOnClickListener(v->{

            String pet_id = "error_id";
            // pet_id0
            pet_id = profile.getPet_id0().toString();
            if(!pet_id.equals("error_id")){
                i.putExtra("pet_id", pet_id);
            }
            startActivity(i);
        });
        profile_pet2.setOnClickListener(v->{

            String pet_id = "error_id";
            // pet_id1
            pet_id = profile.getPet_id1().toString();
            if(!pet_id.equals("error_id")){
                i.putExtra("pet_id", pet_id);
            }
            startActivity(i);
        });
        profile_pet3.setOnClickListener(v->{

            String pet_id = "error_id";
            // pet_id2
            pet_id = profile.getPet_id2().toString();
            if(!pet_id.equals("error_id")){
                i.putExtra("pet_id", pet_id);
            }
            startActivity(i);
        });

    }
}

