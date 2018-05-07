package com.petclump.petclump;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class PetInfoActivity extends AppCompatActivity implements ImageView.OnClickListener{
    private static final String TAG = "Pet Info Activity";
    private TextView pet_name, pet_age, pet_bio, pet_primary_name, pet_and_I_name;
    private Spinner pet_specie;
    private String[] specie_array_string;
    private int sequence = -1;
    private ImageView pet_view_main, pet_view_1, pet_view_2, pet_view_3, pet_view_4, pet_view_5,
            group_view_1, group_view_2, group_view_3;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context c;

    Button Button_to_quiz, Button_return, Button_save, Button_delete;
    private PetProfile pet = new PetProfile();
    private static final int INTIAL_CODE = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);
        Bundle extras = getIntent().getExtras();
        sequence = (Integer) extras.get("sequence");
        Log.d(TAG,"sequence:"+sequence);

        setupUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode > INTIAL_CODE){
            //pet_view_5.setImageURI();

            int code = requestCode;
            Uri imageUrl = data.getData();
            ImageView t;
            switch(code){
                case 100:
                    t = pet_view_main;
                    pet.deletePhoto("main_profile_url",()->{});
                    pet_view_main.setImageURI(imageUrl);
                    break;
                case 101:
                    t = pet_view_1;
                    pet.deletePhoto("pet_profile_url_1",()->{});
                    pet_view_1.setImageURI(imageUrl);
                    break;
                case 102:
                    t = pet_view_2;
                    pet.deletePhoto("pet_profile_url_2",()->{});
                    pet_view_2.setImageURI(imageUrl);
                    break;
                case 103:
                    t = pet_view_3;
                    pet.deletePhoto("pet_profile_url_3",()->{});
                    pet_view_3.setImageURI(imageUrl);
                    break;
                case 104:
                    t = pet_view_4;
                    pet.deletePhoto("pet_profile_url_4",()->{});
                    pet_view_4.setImageURI(imageUrl);
                    break;
                case 105:
                    t = pet_view_5;
                    pet.deletePhoto("pet_profile_url_5",()->{});
                    pet_view_5.setImageURI(imageUrl);
                    break;
                case 111:
                    t = group_view_1;
                    pet.deletePhoto("group_profile_url_1",()->{});
                    group_view_1.setImageURI(imageUrl);
                   break;
                case 112:
                    t = group_view_2;
                    pet.deletePhoto("group_profile_url_2",()->{});
                    group_view_2.setImageURI(imageUrl);
                    break;
                case 113:
                    t = group_view_3;
                    pet.deletePhoto("group_profile_url_3",()->{});
                    group_view_3.setImageURI(imageUrl);
                    break;
                default: Log.w(TAG,"PHOTOT GALLERY RESULT called on unknown request code "+ code);
                    return;
            }
            t.setDrawingCacheEnabled(true);
            t.buildDrawingCache();
            Bitmap bitmap = t.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            t.setDrawingCacheEnabled(false);
            byte[] d = baos.toByteArray();
            pet.setPhoto(t.getTag().toString(),d,()->{
                Toast.makeText(getApplicationContext(),"Upload Successful! Please press SAVE to confirm.",Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupUI(){

        Button_to_quiz = findViewById(R.id.Button_to_quiz);
        Button_return = findViewById(R.id.Button_return);
        Button_save = findViewById(R.id.button_save);
        Button_delete = findViewById(R.id.Button_delete_pet);

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
        // download information
        pet.download(user.getUid()+sequence, () -> {
            pet_primary_name.setText(pet.getName());
            pet_and_I_name.setText(pet.getName()+" and I");
            pet_age.setText(pet.getAge());
            pet_bio.setText(pet.getBio());
            pet_specie.setSelection(Specie.num_specie(pet.getSpe().toString()));
            pet_name.setText(pet.getName());

            // setup picture URL
            String [] url = new String[]{
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
            for(int k=0; k<9; k++){
                if(url[k] != null && url[k].compareTo("") != 0){
                    new DownloadImageTask(im[k]).execute(url[k]);
                }
            }
/*           for(String x: url){
                Log.d(TAG, "url list:" +x);
            }*/
        });

        Button_to_quiz.setOnClickListener(v ->
                startActivity(new Intent(this, QuizActivity.class))
        );
        Button_return.setOnClickListener(v ->
                finish()
        );
        Button_delete.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                .setTitle("Delete your pet?")
                .setMessage("Are your sure you want to delete your pet?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", (DialogInterface dialog, int which)->{
                    pet.delete(user.getUid()+sequence,()->{
                        Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                    });
                    finish();
                })
                .setNegativeButton("No", null).show();

            });


//        Button_edit.setOnClickListener(v->{
//            Intent i = new Intent(this, PetInfoEditActivity.class);
//            i.putExtra("sequence", sequence);
//            startActivity(i);
//        });
        // upload pet information
        Button_save.setOnClickListener(v->{
            pet.setBio(pet_bio.getText().toString());
            pet.setAge(pet_age.getText().toString());
            pet.setName(pet_name.getText().toString());
            //pet.setSpe(pet_specie.getText().toString());
            pet.setOwner_id(user.getUid());
            pet.setSequence(sequence);
            pet.setSpe(Specie.specie_num(getSpinnerPosition(pet_specie, pet_specie.getSelectedItem())));
            pet.upload(user.getUid()+sequence,()->{
                Toast.makeText(this, "Upload Complete!", Toast.LENGTH_SHORT).show();
            });
        });
    }
    private Integer getSpinnerPosition(Spinner spinner, Object item){
        return ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(item.toString());
    }
    private void openGallery(int code){
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
                .setPositiveButton("Create", (DialogInterface dialog, int which)->{
                    int code = INTIAL_CODE;
                    switch(v.getTag().toString()){
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
                        default: Log.w(TAG,"PHOTOT GALLERY RESULT called on unknown tag ");
                            return;
                    }
                    openGallery(code);
                })
                .setNegativeButton("Delete", (DialogInterface dialog, int which)->{
                    pet.deletePhoto (v.getTag().toString(), ()->{
                        Toast.makeText(getApplicationContext(),"Delete successfully! Press SAVE to confirm!",Toast.LENGTH_SHORT).show();
                    });
                })
                .setNeutralButton("Cancel", null).show();

        // open gallery

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
