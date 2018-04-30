package com.petclump.petclump;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    String pet_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info_edit);

        // used to judge if its
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            pet_id = extras.getString("pet_id");
        }

        setupUI();
    }

    private void saveData() {

    }

    private void setupUI(){
        specie_array_string = new String[19];
        pet_name_editText = findViewById(R.id.pet_name_editText);
        pet_age_editText = findViewById(R.id.pet_age_editText);
        pet_bio_editText = findViewById(R.id.pet_bio_editText);

        pet_specie = findViewById(R.id.pet_specie);
        int i = 0;
        for (Specie s: Specie.values()){
            specie_array_string[i] = s.getName(this);
            i+=1;
        }

        ArrayAdapter<String> adapter_specie = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, specie_array_string);
        adapter_specie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pet_specie.setAdapter(adapter_specie);

        saveData();

    }
}
