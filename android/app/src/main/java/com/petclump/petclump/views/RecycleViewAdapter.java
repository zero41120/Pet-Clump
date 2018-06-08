package com.petclump.petclump.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petclump.petclump.controller.MatchingViewProfileActivity;
import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.MatchingProfile;
import com.petclump.petclump.models.PetProfile;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private List<MatchingProfile> pets;
    private Context mContext;
    private String MainPet_id = "";

    public RecycleViewAdapter(Context c, List<MatchingProfile> pets, String MainPet_id) {
        this.mContext = c;
        this.pets = pets;
        this.MainPet_id = MainPet_id;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Style for the grid
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.matchview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // User the position to get the profile to show a card
        holder.pet_matchview_location.setText(pets.get(position).getDistance() + "km");
        holder.pet_matchview_label.setText(pets.get(position).getMatchingPercent()+ "%");

        String url = pets.get(position).getPhotoUrl();
        if(url.compareTo("") != 0){
            new DownloadImageTask(holder.pet_matchview_image,mContext).execute(url);
        }else{
            holder.pet_matchview_image.setImageResource(PetProfile.default_image);
        }

        holder.matchview_cardView.setOnClickListener(v->{
            Intent intent = new Intent(mContext, MatchingViewProfileActivity.class);
            intent.putExtra("petId", pets.get(position).getPetId());
            intent.putExtra("MainPetId", MainPet_id);
            //intent.putExtra("main_url", pets.get(position).getPhotoUrl());
            mContext.startActivity(intent);
        });
}

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pet_matchview_label;
        TextView pet_matchview_location;
        ImageView pet_matchview_image;
        CardView matchview_cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            pet_matchview_location = itemView.findViewById(R.id.pet_matchview_location);
            pet_matchview_image = itemView.findViewById(R.id.pet_matchview_image);
            pet_matchview_label = itemView.findViewById(R.id.pet_matchview_label);
            matchview_cardView= itemView.findViewById(R.id.matchview_cardView);
        }
    }

}
