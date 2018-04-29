package com.petclump.petclump;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petclump.petclump.models.OwnerProfile;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.Specie;

import java.util.GregorianCalendar;

public class PetInfoEditActivity extends AppCompatActivity {
    private static final String TAG = "PetInfoEdit Activity";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context c;
=======
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.petclump.petclump.models.Specie;

public class PetInfoEditActivity extends AppCompatActivity {
    EditText pet_name_editText, pet_age_editText, pet_bio_editText;
    Button button_cancel, button_save;
    Spinner pet_specie;
    String specie_array_string[];
>>>>>>> 4f62509da21db42009f66dac29d076d31695d02b

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        if(user == null){
            Log.d(TAG, "onCreate: User not logged in");
            finish();
        }
        setupUI();
        downloadData();
    }
    private void setupUI(){
        setContentView(R.layout.activity_pet_info_view);
        c = getApplicationContext();
        saveData();

    }
    private void downloadData(){

    }
    private void saveData() {
        PetProfile profile = new PetProfile();
        profile.setName("123");
        profile.setBio("i am from earth");
        profile.setAge("89");
        profile.setSpe(Specie.CAT.getName(c));

        profile.upload(user.getUid(), c);
=======
        setContentView(R.layout.activity_pet_info_edit);

        setupUI();

    }
    private void setupUI(){
        specie_array_string = new String[19];
        pet_name_editText = findViewById(R.id.pet_name_editText);
        pet_age_editText = findViewById(R.id.pet_age_editText);
        pet_bio_editText = findViewById(R.id.pet_bio_editText);
        button_save = findViewById(R.id.)

        pet_specie = findViewById(R.id.pet_specie);
        int i = 0;
        for (Specie s: Specie.values()){
            specie_array_string[i] = s.getName(this);
            i+=1;
        }

        ArrayAdapter<String> adapter_specie = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, specie_array_string);
        adapter_specie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pet_specie.setAdapter(adapter_specie);



>>>>>>> 4f62509da21db42009f66dac29d076d31695d02b
    }
}
