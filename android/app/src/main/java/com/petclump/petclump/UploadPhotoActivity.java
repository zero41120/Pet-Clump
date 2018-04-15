package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UploadPhotoActivity extends AppCompatActivity {

    private static final String TAG = "UploadPhotoActivity";
    private static int RESULT_LOAD_IMAGE = 1;
    private static String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private String      photoPath;
    private ImageView   downloadView, uploadView;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    /**
     * This method checks if given permissions are granted. If not, this method will
     * requests the permissions.
     * @param permissions this is a string array with the permission names.
     */
    private void checkPermissions(String... permissions) {
        Context context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions,5);
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        checkPermissions(permissions);

        downloadView                = findViewById(R.id.downloadView);
        uploadView                  = findViewById(R.id.uploadView);
        Button buttonPickPicture    = findViewById(R.id.button_pick_picture);
        Button buttonDownload       = findViewById(R.id.button_download_picture);
        Button buttonUpload         = findViewById(R.id.button_upload_picture);

        buttonPickPicture.setOnClickListener( v -> pickImage());
        buttonDownload.setOnClickListener( v -> downloadImage());
        buttonUpload.setOnClickListener(v -> uploadImage());
    }

    /**
     * This method download this image images/IMG_20180414_213521.jpg from firebase
     * and save that to a temp file.
     */
    private void downloadImage(){
        String path = "images/IMG_20180414_213521.jpg";
        try{
            File localFile = File.createTempFile("images", "jpg");

            StorageReference storageRef = storage.getReference();

            storageRef.child(path).getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> loadImageToView(downloadView, localFile))
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * This method uploads an image to Firebase. Firebase is set to insecure public read/write.
     */
    private void uploadImage(){
        if (photoPath == null || photoPath.equals("")) {
            Toast.makeText(this, "No photo path", Toast.LENGTH_SHORT).show();
            return;
        }

        // File or Blob
        Uri file = Uri.fromFile(new File(photoPath));

        // Create the file metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/png")
                .build();

        // Upload file and metadata to the path 'images/mountains.jpg'
        StorageReference storageRef = storage.getReference();
        UploadTask uploadTask = storageRef.child("images/" + file.getLastPathSegment()).putFile(file, metadata);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                })
                .addOnPausedListener(taskSnapshot ->
                    System.out.println("Upload is paused")
                )
                .addOnFailureListener(e ->
                        Log.d(TAG, "uploadImage: " + e.getMessage())
                )
                .addOnSuccessListener(taskSnapshot ->
                    Toast.makeText(UploadPhotoActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show()
                );


    }

    /**
     * This method starts an activity to get image
     */
    private void pickImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    /**
     * This method is called when image is successful send from other activity to our app
     * @param data the Intent object with image data
     */
    private void loadImageToView(ImageView imageView, Intent data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage == null) {
                return;
            }

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor == null){
                return;
            }
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            this.photoPath = picturePath;

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    /**
     * This method is called when image is successful send from other activity to our app
     * @param data the File object with image data
     */
    private void loadImageToView(ImageView imageView, File data){
        imageView.setImageBitmap(BitmapFactory.decodeFile(data.getAbsolutePath()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            loadImageToView(uploadView, data);
        }
    }
}
