package com.petclump.petclump.controller;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.QuizQuestion;
import com.petclump.petclump.models.Specie;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class PetInfoActivity extends AppCompatActivity implements ImageView.OnClickListener {
    private static final String TAG = "PetInfoActivity";
    private TextView pet_name, pet_age, pet_bio, pet_primary_name, pet_and_I_name, Quiz_number;
    private TextView cha_tracker;
    private Spinner pet_specie;
    private String[] specie_array_string;
    private int sequence = -1;
    private ImageView pet_view_main, pet_view_1, pet_view_2, pet_view_3, pet_view_4, pet_view_5,
            group_view_1, group_view_2, group_view_3;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context c;
    private TextWatcher textWatcher;
    Button Button_to_quiz, Button_cancel, Button_save, Button_delete;
    private PetProfile pet = new PetProfile();
    private static final int INTIAL_CODE = 99;
    private boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = getApplicationContext();
        setContentView(R.layout.activity_pet_info);
        Bundle extras = getIntent().getExtras();
        sequence = (Integer) extras.get("sequence");
        Log.d(TAG, "sequence:" + sequence);
        setActionBar(String.valueOf(getText(R.string.Pet_info)));
        //keyboardOff();
        isSaved = false;
        setupUI();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode > INTIAL_CODE) {
            //pet_view_5.setImageURI();

            int code = requestCode;
            Uri imageUrl = data.getData();
            ImageView t;
            switch (code) {
                case 100:
                    t = pet_view_main;
                    pet.deletePhoto("main_profile_url", () -> {
                    });
                    pet_view_main.setImageURI(imageUrl);
                    break;
                case 101:
                    t = pet_view_1;
                    pet.deletePhoto("pet_profile_url_1", () -> {
                    });
                    pet_view_1.setImageURI(imageUrl);
                    break;
                case 102:
                    t = pet_view_2;
                    pet.deletePhoto("pet_profile_url_2", () -> {
                    });
                    pet_view_2.setImageURI(imageUrl);
                    break;
                case 103:
                    t = pet_view_3;
                    pet.deletePhoto("pet_profile_url_3", () -> {
                    });
                    pet_view_3.setImageURI(imageUrl);
                    break;
                case 104:
                    t = pet_view_4;
                    pet.deletePhoto("pet_profile_url_4", () -> {
                    });
                    pet_view_4.setImageURI(imageUrl);
                    break;
                case 105:
                    t = pet_view_5;
                    pet.deletePhoto("pet_profile_url_5", () -> {
                    });
                    pet_view_5.setImageURI(imageUrl);
                    break;
                case 111:
                    t = group_view_1;
                    pet.deletePhoto("group_profile_url_1", () -> {
                    });
                    group_view_1.setImageURI(imageUrl);
                    break;
                case 112:
                    t = group_view_2;
                    pet.deletePhoto("group_profile_url_2", () -> {
                    });
                    group_view_2.setImageURI(imageUrl);
                    break;
                case 113:
                    t = group_view_3;
                    pet.deletePhoto("group_profile_url_3", () -> {
                    });
                    group_view_3.setImageURI(imageUrl);
                    break;
                default:
                    Log.w(TAG, "PHOTOT GALLERY RESULT called on unknown request code " + code);
                    return;
            }
            t.setDrawingCacheEnabled(true);
            t.buildDrawingCache();
            Bitmap bitmap = t.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            t.setDrawingCacheEnabled(false);
            byte[] d = baos.toByteArray();
            pet.setPhoto(t.getTag().toString(), d, () -> {
                Toast.makeText(getApplicationContext(), "Upload Successful! Please press SAVE to confirm.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupUI() {
        cha_tracker = findViewById(R.id.cha_tracker);
        Button_to_quiz = findViewById(R.id.Button_to_quiz);
        //Button_return = findViewById(R.id.Button_return);
        Button_delete = findViewById(R.id.Button_delete_pet);
        Quiz_number = findViewById(R.id.Quiz_number);

        pet_view_main = findViewById(R.id.pet_main_profile);
        pet_view_1 = findViewById(R.id.pet_profile_1);
        pet_view_2 = findViewById(R.id.pet_profile_2);
        pet_view_3 = findViewById(R.id.pet_profile_3);
        pet_view_4 = findViewById(R.id.pet_profile_4);
        pet_view_5 = findViewById(R.id.pet_profile_5);
        group_view_1 = findViewById(R.id.PO_profile_1);
        group_view_2 = findViewById(R.id.PO_profile_2);
        group_view_3 = findViewById(R.id.PO_profile_3);

        // set up UI
        pet_age = findViewById(R.id.pet_age);
        pet_bio = findViewById(R.id.pet_bio);
        pet_name = findViewById(R.id.pet_name);
        pet_specie = findViewById(R.id.pet_specie);

        // set_up pet_view listener
        pet_view_main.setOnClickListener(this);
        pet_view_1.setOnClickListener(this);
        pet_view_2.setOnClickListener(this);
        pet_view_3.setOnClickListener(this);
        pet_view_4.setOnClickListener(this);
        pet_view_5.setOnClickListener(this);
        group_view_1.setOnClickListener(this);
        group_view_2.setOnClickListener(this);
        group_view_3.setOnClickListener(this);

        specie_array_string = new String[19];
        int i = 0;
        for (Specie s : Specie.values()) {
            specie_array_string[i] = s.getName(this);
            i += 1;
        }
        ArrayAdapter<String> adapter_specie = new ArrayAdapter<>(this,
                R.layout.spinner_item, specie_array_string);
        adapter_specie.setDropDownViewResource(android.R.layout.simple_spinner_item);
        pet_specie.setAdapter(adapter_specie);

        pet_primary_name = findViewById(R.id.title_pet_name);
        pet_and_I_name = findViewById(R.id.Txt_my_pet_and_I);
        // download information
        pet.download(user.getUid() + sequence, () -> {
            pet_primary_name.setText(pet.getName());
            pet_and_I_name.setText(pet.getName() + " and I");
            pet_age.setText(pet.getAge());
            pet_bio.setText(pet.getBio());
            pet_specie.setSelection(Specie.num_specie(pet.getSpe().toString()));
            pet_name.setText(pet.getName());
            String quiz = pet.getQuiz();
            Integer quiz_num = quiz.length();
            if (quiz.length()==70){
                Quiz_number.setText("Quiz completion: 70 out of 70");
                Button_to_quiz.setBackgroundResource(R.drawable.cancel_button_background);
                Button_to_quiz.setText("No quiz available");
            }else{
                Quiz_number.setText("Quiz completion: " + quiz_num.toString() + " out of 70");
            }


            // setup picture URL
            String[] url = new String[]{
                    pet.getUrl("main_profile_url"),
                    pet.getUrl("pet_profile_url_1"),
                    pet.getUrl("pet_profile_url_2"),
                    pet.getUrl("pet_profile_url_3"),
                    pet.getUrl("pet_profile_url_4"),
                    pet.getUrl("pet_profile_url_5"),
                    pet.getUrl("group_profile_url_1"),
                    pet.getUrl("group_profile_url_2"),
                    pet.getUrl("group_profile_url_3")};
            ImageView[] im = new ImageView[]{
                    pet_view_main,
                    pet_view_1,
                    pet_view_2,
                    pet_view_3,
                    pet_view_4,
                    pet_view_5,
                    group_view_1,
                    group_view_2,
                    group_view_3
            };
            // download the url
            for (int k = 0; k < 9; k++) {
                if (url[k] != null && url[k].compareTo("") != 0) {
                    new DownloadImageTask(im[k],this).execute(url[k]);
                }
            }
            cha_tracker.setText(String.valueOf(pet_bio.getText().length()) + "/500");
            textWatcher= new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //This sets a textview to the current length
                    cha_tracker.setText(String.valueOf(s.length())+"/500");
                }

                public void afterTextChanged(Editable s) {
                }
            };
            pet_bio.addTextChangedListener(textWatcher);
/*           for(String x: url){
                Log.d(TAG, "url list:" +x);
            }*/
        });


        Button_to_quiz.setOnClickListener(v -> {
            if (pet.getQuiz().length() == QuizQuestion.getNumberOfAvaliableQuestions(c)) {
                Toast.makeText(c, "You have answered all questions", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(this, QuizActivity.class) {{
                    putExtra("sequence", sequence);
                    Log.d(TAG, String.valueOf(sequence));
                }});
            }
        });


        Button_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Delete your pet?")
                    .setMessage("Are your sure you want to delete your pet?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", (DialogInterface dialog, int which) -> {
                        pet.delete(user.getUid() + sequence, () -> {
                            Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                        });
                        finish();
                    })
                    .setNegativeButton("No", null).show();

        });

    }
    private void saveInfo(){

        pet.setBio(pet_bio.getText().toString());
        pet.setAge(pet_age.getText().toString());
        pet.setName(pet_name.getText().toString());
        //pet.setSpe(pet_specie.getText().toString());
        pet.setOwner_id(user.getUid());
        pet.setSequence(sequence);
        pet.setSpe(Specie.specie_num(getSpinnerPosition(pet_specie, pet_specie.getSelectedItem())));
        pet.upload(user.getUid() + sequence, () -> {
            Toast.makeText(this, "Upload Complete!", Toast.LENGTH_SHORT).show();
            isSaved = true;
        });
    }

    private Integer getSpinnerPosition(Spinner spinner, Object item) {
        return ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(item.toString());
    }

    private void openGallery(int code) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, code);
    }

    // pet_photo settings
    @Override
    public void onClick(View v) {
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Set your picture")
                .setMessage("Image profile setup")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Create", (DialogInterface dialog, int which) -> {
                    int code = INTIAL_CODE;
                    switch (v.getTag().toString()) {
                        case "main_profile_url":
                            code = 100;
                            break;
                        case "pet_profile_url_1":
                            code = 101;
                            break;
                        case "pet_profile_url_2":
                            code = 102;
                            break;
                        case "pet_profile_url_3":
                            code = 103;
                            break;
                        case "pet_profile_url_4":
                            code = 104;
                            break;
                        case "pet_profile_url_5":
                            code = 105;
                            break;
                        case "group_profile_url_1":
                            code = 111;
                            break;
                        case "group_profile_url_2":
                            code = 112;
                            break;
                        case "group_profile_url_3":
                            code = 113;
                            break;
                        default:
                            Log.w(TAG, "PHOTOT GALLERY RESULT called on unknown tag ");
                            return;
                    }
                    openGallery(code);
                })
                .setNegativeButton("Delete", (DialogInterface dialog, int which) -> {
                    pet.deletePhoto(v.getTag().toString(), () -> {
                        Toast.makeText(getApplicationContext(), "Delete successfully! Press SAVE to confirm!", Toast.LENGTH_SHORT).show();
                    });
                })
                .setNeutralButton("Cancel", null).show();

        // open gallery

    }
    public boolean noInfo(){
        String s = "";
        String pn = String.valueOf(pet_name.getText());
        String pa = String.valueOf(pet_age.getText());
        String pb = String.valueOf(pet_bio.getText());
        boolean infoCheck = s.equalsIgnoreCase(pn) || s.equalsIgnoreCase(pa) || s.equalsIgnoreCase(pb);
        return infoCheck;
    }
    public boolean noPetImage(){
        if (pet_view_main.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.photo_placeholder).getConstantState()){
            return true;
        }
        return false;
    }

    public boolean noOwnerImage(){
        Object o = getResources().getDrawable(R.drawable.photo_placeholder).getConstantState();
        boolean one = group_view_1.getDrawable().getConstantState()==o;
        boolean two = group_view_2.getDrawable().getConstantState()==o;
        boolean three = group_view_3.getDrawable().getConstantState()==o;
        boolean imageCheck = one || two || three;
        return imageCheck;
    }
    public void getDialogForText(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("wait!")
                .setMessage("Please fill in the required info.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", (DialogInterface dialog, int which) -> {
                })
                .setNegativeButton("Ok", null).show();
    }
    public void getDialogForImg(boolean isPet){
        if (isPet){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("wait!")
                    .setMessage("Please at least upload the main pet photo.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", (DialogInterface dialog, int which) -> {
                    })
                    .setNegativeButton("Ok", null).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("wait!")
                    .setMessage("Please at least upload one owner photo.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", (DialogInterface dialog, int which) -> {
                    })
                    .setNegativeButton("Ok", null).show();
        }


    }

    public void getDialogUnsaved(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("wait!")
                .setMessage("Are you sure you want to exit? You haven't saved yet!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", (DialogInterface dialog, int which) -> {
                    finish();
                })
                .setNegativeButton("No", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);

    }
    public void setActionBar(String heading) {
        // TODO Auto-generated method stub

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView myText = findViewById(R.id.mytext);
        myText.setText(heading);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                saveInfo();
                break;
            case android.R.id.home:
                if (isSaved==false){
                    getDialogUnsaved();
                } else{
                    if (noInfo()){
                        getDialogForText();
                    }else if(noPetImage()){
                        getDialogForImg(true);
                    }else if(noOwnerImage()){
                        getDialogForImg(false);
                    }else{
                        finish();
                    }
                }
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

}
