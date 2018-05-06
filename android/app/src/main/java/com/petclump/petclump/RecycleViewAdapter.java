package com.petclump.petclump;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private Context mContext;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.matchview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.pet_matchview_label.setText("hey");
        holder.pet_matchview_image.setImageResource(R.drawable.dog_placeholder);
        holder.matchview_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
}

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pet_matchview_label;
        ImageView pet_matchview_image;
        CardView matchview_cardView;
        public MyViewHolder(View itemView) {
            super(itemView);

            pet_matchview_image = itemView.findViewById(R.id.pet_matchview_image);
            pet_matchview_label = itemView.findViewById(R.id.pet_matchview_image);
            matchview_cardView= itemView.findViewById(R.id.matchview_cardView);
        }
    }
}
