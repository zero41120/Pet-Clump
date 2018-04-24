package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String day_array_string[];
    String year_array_string[];
    private int year;
    EditText user_name_editText;
    Button  save_button, cancel_button;
    TextView match_range_value;
    SeekBar user_match_range_seekbar;
    Spinner user_dob_day, user_dob_month, user_dob_year, user_select_gender;
    Object dob_year, dob_month, dob_day, gender;
    Context c;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);


        c = getApplicationContext();

        constraintSet = new ConstraintSet();
        constraintLayout = findViewById(R.id.user_info_edit_layout);
        constraintSet.clone(constraintLayout);

        user_name_editText = findViewById(R.id.user_name_editText);
        match_range_value = findViewById(R.id.match_range_value);
        user_match_range_seekbar = findViewById(R.id.user_match_range);

        user_dob_day = findViewById(R.id.user_dob_day);
        user_dob_day.setOnItemSelectedListener(this);
        user_dob_month = findViewById(R.id.user_dob_month);
        user_dob_month.setOnItemSelectedListener(this);
        user_dob_year = findViewById(R.id.user_dob_year);
        user_dob_year.setOnItemSelectedListener(this);
        user_select_gender = findViewById(R.id.user_gender_spinner);
        user_select_gender.setOnItemSelectedListener(this);

        save_button = findViewById(R.id.edit_button);
        cancel_button = findViewById(R.id.cancel_button);

        year = 1928;
        day_array_string = new String[31];
        year_array_string = new String[90];
        for (int i = 0; i < 31; i++) {
            day_array_string[i] = String.valueOf(i + 1);
        }

        for (int i = 0; i < 75; i++) {
            year_array_string[i] = String.valueOf(year);
            year += 1;
        }


        //edit_name_button.setOnClickListener(v ->nameChangeClick());
        save_button.setOnClickListener(v -> saveData());
        cancel_button.setOnClickListener(v -> cancelData());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_month = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter_day = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, day_array_string);
        ArrayAdapter<String> adapter_year = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, year_array_string);
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        user_dob_month.setAdapter(adapter_month);
        user_dob_day.setAdapter(adapter_day);
        user_dob_year.setAdapter(adapter_year);
        user_select_gender.setAdapter(adapter_gender);

        user_match_range_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (0 == progress) {
                    match_range_value.setText(getResources().getString(R.string.Within_5_miles));
                } else if (1 == progress) {
                    match_range_value.setText(getResources().getString(R.string.Within_20_miles));
                } else if (2 == progress) {
                    match_range_value.setText(getResources().getString(R.string.Within_100_miles));
                } else {
                    match_range_value.setText(getResources().getString(R.string.No_preferred_range));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void cancelData() {
        // switch activity to the main page without saving the data
            Intent nextScreen = new Intent(c, UserInfoActivity.class);
            startActivity(nextScreen);
    }

    private void saveData() {
        Intent nextScreen = new Intent(c, UserInfoActivity.class);
        startActivity(nextScreen);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        switch (parent.getId()) {
            case R.id.user_dob_month:
                dob_month = parent.getItemAtPosition(pos);
            case R.id.user_dob_day:
                dob_day = parent.getItemAtPosition(pos);
                Toast toast2 = Toast.makeText(c, (String) dob_day, Toast.LENGTH_LONG);
                toast2.show();

            case R.id.user_dob_year:
                dob_year = parent.getItemAtPosition(pos);
                Toast toast3 = Toast.makeText(c, (String) dob_year, Toast.LENGTH_LONG);
                toast3.show();

            case R.id.user_gender_spinner:
                gender = parent.getItemAtPosition(pos);
                Toast toast4 = Toast.makeText(c, (String) gender, Toast.LENGTH_LONG);
                toast4.show();

        }
        String string = dob_year.toString()+" "+
                dob_month.toString()+" "+dob_day.toString()+" "+gender.toString();
        Toast toast5 = Toast.makeText(c, string, Toast.LENGTH_LONG );
        toast5.show();


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}