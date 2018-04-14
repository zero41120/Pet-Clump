package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UploadPhotoActivity extends AppCompatActivity {

    private static final String TAG = "UploadPhotoActivity";
    private static int RESULT_LOAD_IMAGE = 1;
    private static String[] permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE};
    private String photoPath;
    FirebaseStorage storage = FirebaseStorage.getInstance();

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

        findViewById(R.id.button_pick_picture).setOnClickListener( v -> pickImage());

        findViewById(R.id.button_upload_picture).setOnClickListener(v -> uploadImage());

    }

    /**
     * This method uploads an image to Firebase. Firebase is set to insecure public read/write.
     */
    private void uploadImage(){
        if (photoPath.equals("")) {
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
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
            }
        });


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
    private void loadImageToView(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        this.photoPath = picturePath;

        ImageView imageView = findViewById(R.id.photoView);
        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            loadImageToView(data);
        }
    }
}
