package com.petclump.petclump.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    private String TAG = "DownloadImageTask";
    private Context c;
    public DownloadImageTask(ImageView bmImage, Context c) {
            this.bmImage = (ImageView) bmImage;
            this.c = c;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        // first check cache. If exist, use it.
        File path = new File(c.getCacheDir(),PetProfile.parseUrlToCache(urldisplay));
        if(path.exists()){
            FileInputStream in = null;
            try {
                in = new FileInputStream(path);
                BufferedInputStream buf = new BufferedInputStream(in);
                byte[] bMapArray= new byte[buf.available()];
                buf.read(bMapArray);
                Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
                in.close();
                return bMap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // download the file and cache.
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
        //create a file to write bitmap data
        try {
            path.createNewFile();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mIcon11.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return the file
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
