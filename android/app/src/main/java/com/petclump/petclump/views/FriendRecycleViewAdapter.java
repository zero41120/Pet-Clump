package com.petclump.petclump.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petclump.petclump.R;
import com.petclump.petclump.controller.ChattingActivity;
import com.petclump.petclump.controller.FriendProfileActivity;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.FriendProfile;

import java.util.List;

public class FriendRecycleViewAdapter extends RecyclerView.Adapter<FriendRecycleViewAdapter.MyViewHolder> {
    private List<FriendProfile> friends;
    private Context mContext;


    public FriendRecycleViewAdapter(Context c, List<FriendProfile> friends){
        this.mContext = c;
        this.friends = friends;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Style for the grid
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.friendview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRecycleViewAdapter.MyViewHolder holder, int position) {
        String my_id = friends.get(position).getMy_id();
        String friend_id = friends.get(position).getFriend_id();
        String name = friends.get(position).getName();
        holder.friendview_name.setText(name);
        String time = friends.get(position).getTime();
        holder.friendview_time.setText(time);
        String history = friends.get(position).getLastMessage();
        holder.friendview_history.setText(history);
        String url = friends.get(position).getUrl();
        new DownloadImageTask(holder.friendview_image, mContext).execute(url);
        holder.friendview_image.setOnClickListener(v->{
            Intent intent2 = new Intent(mContext, FriendProfileActivity.class);
            intent2.putExtra("friend_id", friend_id);
            intent2.putExtra("my_id", my_id);
            mContext.startActivity(intent2);
        });

        holder.friendview_cardView.setOnClickListener(v->{
            Intent intent = new Intent(mContext, ChattingActivity.class);
            intent.putExtra("my_id", my_id);
            intent.putExtra("friend_id", friend_id);
            intent.putExtra("Name", name);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView friendview_name, friendview_time, friendview_history;
        ImageView friendview_image;
        CardView friendview_cardView;
        public MyViewHolder(View itemView) {
            super(itemView);

            friendview_name = itemView.findViewById(R.id.friendview_name);
            friendview_time = itemView.findViewById(R.id.friendview_time);
            friendview_history = itemView.findViewById(R.id.friendview_history);
            friendview_image = itemView.findViewById(R.id.friendview_image);
            friendview_cardView = itemView.findViewById(R.id.friendview_cardView);
        }
    }
}
