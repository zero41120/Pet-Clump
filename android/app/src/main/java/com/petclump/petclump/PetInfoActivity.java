package com.petclump.petclump;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petclump.petclump.models.OwnerProfile;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.Specie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class PetInfoActivity extends AppCompatActivity{
    private static final String TAG = "Pet Info Activity";
    private TextView pet_name, pet_age, pet_bio, pet_primary_name, pet_and_I_name;
    private Spinner pet_specie;
    private String[] specie_array_string;
    private int sequence = -1;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context c;

    Button Button_to_quiz, Button_return, Button_save, Button_delete;
    ImageButton Button_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);
        Bundle extras = getIntent().getExtras();
        sequence = (Integer) extras.get("sequence");
        Log.d(TAG,"sequence:"+sequence);


        setupUI();

    }
    private void setupUI(){

        Button_to_quiz = findViewById(R.id.Button_to_quiz);
        Button_return = findViewById(R.id.Button_return);
        Button_save = findViewById(R.id.button_save);
        Button_delete = findViewById(R.id.Button_delete_pet);

        // set up UI
        pet_age = findViewById(R.id.pet_age);
        pet_bio = findViewById(R.id.pet_bio);
        pet_name = findViewById(R.id.pet_name);
        pet_specie = findViewById(R.id.pet_specie);

        specie_array_string = new String[19];
        int i = 0;
        for (Specie s: Specie.values()){
            specie_array_string[i] = s.getName(this);
            i+=1;
        }
        ArrayAdapter<String> adapter_specie = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, specie_array_string);
        adapter_specie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pet_specie.setAdapter(adapter_specie);

        pet_primary_name = findViewById(R.id.title_pet_name);
        pet_and_I_name = findViewById(R.id.Txt_my_pet_and_I);

        PetProfile profile = new PetProfile();
        profile.download(user.getUid()+sequence, () -> {
            pet_primary_name.setText(profile.getName());
            pet_and_I_name.setText(profile.getName()+" and I");
            pet_age.setText(profile.getAge());
            pet_bio.setText(profile.getBio());
            pet_specie.setSelection(Specie.num_specie(profile.getSpe().toString()));
            pet_name.setText(profile.getName());
        });

        Button_to_quiz.setOnClickListener(v ->
                startActivity(new Intent(this, QuizActivity.class))
        );
        Button_return.setOnClickListener(v ->
                finish()
        );
        Button_delete.setOnClickListener(v-> new PetProfile().delete(user.getUid()+sequence,()->{
            //Toast.makeText(getApplicationContext(), "Delete successfully!",Toast.LENGTH_SHORT);
        }) );
//        Button_edit.setOnClickListener(v->{
//            Intent i = new Intent(this, PetInfoEditActivity.class);
//            i.putExtra("sequence", sequence);
//            startActivity(i);
//        });

        Button_save.setOnClickListener(v->{
            PetProfile pet = new PetProfile();
            pet.setBio(pet_bio.getText().toString());
            pet.setAge(pet_age.getText().toString());
            pet.setName(pet_name.getText().toString());
            //pet.setSpe(pet_specie.getText().toString());
            pet.setOwner_id(user.getUid());
            pet.setSequence(sequence);
            pet.setSpe(Specie.specie_num(getSpinnerPosition(pet_specie, pet_specie.getSelectedItem())));
            pet.upload(user.getUid()+sequence,()->{});
        });
    }
    private Integer getSpinnerPosition(Spinner spinner, Object item){
        return ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(item.toString());
    }


}
