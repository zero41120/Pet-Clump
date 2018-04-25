package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;
import java.util.Map;

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

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(user == null){
            Log.d(TAG, "onCreate: User not logged in");
            finish();
        }
        setupUI();
        downloadData();
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
        range_label     = findViewById(R.id.match_range_seekbar);

        edit_button     = findViewById(R.id.save_button);

        profile_pet1.setOnClickListener(v ->
            startActivity(new Intent(c, PetInfoActivity.class))
        );

        button_add_pet = findViewById(R.id.button_add_pet);
        button_add_pet.setOnClickListener(v -> add_pets());

        button_why = findViewById(R.id.button_why);
        button_why.setOnClickListener(v ->
            startActivity(new Intent(c, Popup.class))
        );

        edit_button.setOnClickListener(v ->
            startActivity(new Intent(c, UserInfoEditActivity.class))
        );
    }

    private void downloadData(){
        if (user == null){ return; }
        String uid = user.getUid();

        DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        mDocRef.addSnapshotListener((snap, error) -> {
            if (error != null) {
                Log.d(TAG, "Download failed: " + error.toString());
                return;
            }
            if (snap == null)   { return; }
            if (!snap.exists()) { return; }
            Map<String, Object> ref = snap.getData();
            name_label.setText(ref.get("name").toString());
            gender_label.setText(ref.get("gender").toString());
            if (ref.get("birthday") instanceof Timestamp){
                Timestamp bd = (Timestamp) ref.get("birthday");
                birthday = bd.getApproximateDate();
                birthday_label.setText(birthday.toString());
            }
            range_label.setText(ref.get("distancePerference").toString());
        });
    }

    private void add_pets() {
        if (1 == number_of_pets) {
            number_of_pets += 1;
            profile_pet2.setVisibility(View.VISIBLE);
            //name_pet2.setVisibility(View.VISIBLE);

        } else if (2 == number_of_pets) {
            number_of_pets += 1;
            profile_pet3.setVisibility(View.VISIBLE);
            //name_pet3.setVisibility(View.VISIBLE);

        } else {//if 3==number
            Toast toast = Toast.makeText(c, "you've reached the maximum pet number!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}

