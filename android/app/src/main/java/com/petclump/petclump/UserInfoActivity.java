package com.petclump.petclump;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class UserInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int number_of_pets = 1;
    private int day_array[], year_array[];
    String day_array_string[];
    String year_array_string[];
    private int year;
    ImageView profile_user;
    ImageButton button_add_pets, button_edit_user_photo;
    CircularImageView profile_pet1, profile_pet2, profile_pet3;
    TextView name_pet1, name_pet2, name_pet3;
    Spinner user_dob_day, user_dob_month, user_dob_year;
    Context c;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;
    DatePickerDialog.OnDateSetListener dob;

    public UserInfoActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        c = getApplicationContext();

        constraintSet = new ConstraintSet();
        constraintLayout = findViewById(R.id.user_info_layout);
        constraintSet.clone(constraintLayout);

        profile_user = findViewById(R.id.profile_user);
        profile_pet1 = findViewById(R.id.profile_pet1);
        profile_pet2 = findViewById(R.id.profile_pet2);
        profile_pet3 = findViewById(R.id.profile_pet3);
        name_pet1 = findViewById(R.id.name_pet1);
        name_pet2 = findViewById(R.id.name_pet2);
        name_pet3 = findViewById(R.id.name_pet3);
        user_dob_day = findViewById(R.id.user_dob_day);
        user_dob_day.setOnItemSelectedListener(this);
        user_dob_month = findViewById(R.id.user_dob_month);
        user_dob_day.setOnItemSelectedListener(this);
        user_dob_year = findViewById(R.id.user_dob_year);
        user_dob_day.setOnItemSelectedListener(this);
        year = 1928;
        day_array_string = new String[31];
        year_array_string = new String[90];
        for (int i=0; i<31; i++){
            day_array_string[i] = String.valueOf(i+1);
        }

        for (int i=0; i<75; i++){
            year_array_string[i] = String.valueOf(year);
            year+=1;
        }

        if (2 == number_of_pets) {
            profile_pet2.setVisibility(View.VISIBLE);
            name_pet2.setVisibility(View.VISIBLE);
        } else if (3 == number_of_pets) {
            profile_pet3.setVisibility(View.VISIBLE);
            profile_pet3.setVisibility(View.VISIBLE);
            name_pet2.setVisibility(View.VISIBLE);
            name_pet3.setVisibility(View.VISIBLE);
        }
        profile_pet1.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, PetInfoActivity.class);
            startActivity(nextScreen);
        });

        button_add_pets = findViewById(R.id.button_add_pet);
        button_add_pets.setOnClickListener(v -> add_pets());

        button_edit_user_photo = findViewById(R.id.button_edit_user_photo);
        button_edit_user_photo.setOnClickListener(v -> edit_photo());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_month = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter_day = new ArrayAdapter<>(this,
               android.R.layout.simple_spinner_item, day_array_string);
        ArrayAdapter<String> adapter_year = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, year_array_string);
        // Specify the layout to use when the list of choices appears
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        user_dob_month.setAdapter(adapter_month);
        user_dob_day.setAdapter(adapter_day);
        user_dob_year.setAdapter(adapter_year);
    }
    private void add_pets() {
        if (1 == number_of_pets) {
              number_of_pets+=1;
              profile_pet2.setVisibility(View.VISIBLE);
              name_pet2.setVisibility(View.VISIBLE);
//
        }else if (2 == number_of_pets) {
            number_of_pets+=1;
            profile_pet3.setVisibility(View.VISIBLE);
            name_pet3.setVisibility(View.VISIBLE);
            number_of_pets+=1;

        }else {//if 3==number
            Toast toast = Toast.makeText(c, "you've reached the maximum pet number!", Toast.LENGTH_LONG);
            toast.show();
        }

    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        Toast toast = Toast.makeText(c, "please select!", Toast.LENGTH_LONG);
        toast.show();
    }

    private void edit_photo() {
        Toast toast = Toast.makeText(c, "edit photo!", Toast.LENGTH_LONG);
        toast.show();
    }


//    public void TextViewClicked(View view) {
//        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
//        switcher.showNext(); //or switcher.showPrevious();
//        TextView myTV = switcher.findViewById(R.id.clickable_text_view);
//        EditText myTX = switcher.findViewById(R.id.hidden_edit_view)
//        //myTV.setText(getString((EditText)myTX));
//        switcher.showPrevious();
//    }
}