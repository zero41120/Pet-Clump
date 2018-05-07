package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.Profile;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 6;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private List<PetProfile> pets;
    private Context mContext;

    public RecycleViewAdapter(MatchingViewActivity matchingViewActivity, List<PetProfile> pets) {
        this.mContext = matchingViewActivity;
        this.pets = pets;
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
        holder.pet_matchview_label.setText(pets.get(position).getName());
        holder.pet_matchview_image.setImageResource(R.drawable.dog_placeholder);
        holder.matchview_cardView.setOnClickListener(v->{
            Intent intent = new Intent(mContext, MatchingViewProfileActivity.class);
            intent.putExtra("Name", pets.get(position).getName());
            intent.putExtra("Age", pets.get(position).getAge());
            intent.putExtra("Bio", pets.get(position).getBio());
            intent.putExtra("Spe", pets.get(position).getSpe()); 
            mContext.startActivity(intent);
        });
}

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pet_matchview_label;
        ImageView pet_matchview_image;
        CardView matchview_cardView;
        public MyViewHolder(View itemView) {
            super(itemView);

            pet_matchview_image = itemView.findViewById(R.id.pet_matchview_image);
            pet_matchview_label = itemView.findViewById(R.id.pet_matchview_label);
            matchview_cardView= itemView.findViewById(R.id.matchview_cardView);
        }
    }

}
