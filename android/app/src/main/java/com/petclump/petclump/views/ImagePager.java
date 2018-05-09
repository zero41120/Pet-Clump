package com.petclump.petclump.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.PetProfile;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ImagePager extends PagerAdapter {
    Context mContext;
    private String pet_id = "";
    private String[] imagesId = new String[]{
        "main_profile_url",
        "pet_profile_url_1",
        "pet_profile_url_2",
        "pet_profile_url_3",
        "pet_profile_url_4",
        "pet_profile_url_5",
        "group_profile_url_1",
        "group_profile_url_2",
        "group_profile_url_3"
    };

    public ImagePager(String id, Context mContext) {
        this.mContext = mContext;
        this.pet_id = id;
    }

    @Override
    public int getCount() {
        return imagesId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==(object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        //String url = "";
        PetProfile pet = new PetProfile();
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        pet.download(this.pet_id, ()->{
            String url = pet.getUrl(imagesId[position]);
            if(url.compareTo("") != 0){
                new DownloadImageTask(imageView).execute(url);
            }else{
                imageView.setImageResource(PetProfile.default_image);
            }
/*            Log.d("ImagePager","url:"+url);
            Log.d("ImagePager","read:"+position+" "+imagesId[position]);
            Log.d("ImagePager","dictionary:"+pet);
            Toast.makeText(mContext, "download successful!", Toast.LENGTH_SHORT).show();*/
        });
        //Log.d("ImagePager","pet_id:"+pet_id);
        //int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        //imageView.setPadding(padding, padding, padding, padding);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
