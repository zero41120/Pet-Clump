package com.petclump.petclump;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.Specie;

public class PetInfoEditActivity extends AppCompatActivity {
    EditText pet_name_editText, pet_age_editText, pet_bio_editText;
    Button cancel_button, save_button;
    Spinner pet_specie;
    String specie_array_string[];
    private int sequence = -1;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info_edit);
        // used to judge if its
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.get("sequence") != null){
            sequence = (Integer) extras.get("sequence");
        }
        setupUI();
    }

    private void saveData() {
        finish();
    }

    private void setupUI(){
        specie_array_string = new String[19];
        pet_name_editText = findViewById(R.id.pet_name_editText);
        pet_age_editText = findViewById(R.id.pet_age_editText);
        pet_bio_editText = findViewById(R.id.pet_bio_editText);
        cancel_button = findViewById(R.id.cancel_button);
        save_button = findViewById(R.id.save_button);

        pet_specie = findViewById(R.id.pet_specie);
        int i = 0;
        for (Specie s: Specie.values()){
            specie_array_string[i] = s.getName(this);
            i+=1;
        }
        save_button.setOnClickListener(v->
           saveData()
        );
        cancel_button.setOnClickListener(v->
            finish());
        ArrayAdapter<String> adapter_specie = new ArrayAdapter<>(this,

                android.R.layout.simple_spinner_item, specie_array_string);
        adapter_specie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pet_specie.setAdapter(adapter_specie);
        PetProfile pet = new PetProfile();
        pet.download(user.getUid()+sequence, ()->{
            pet_name_editText.setText(pet.getName());
            pet_age_editText.setText(pet.getAge());
            pet_bio_editText.setText(pet.getBio());
            pet_specie.setSelection(Specie.num_specie(pet.getSpe().toString()));
        });

    }
}
