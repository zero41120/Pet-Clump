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

import com.petclump.petclump.R;

import java.util.List;

public class QuizRecycleViewAdapter extends RecyclerView.Adapter<QuizRecycleViewAdapter.MyViewHolder> {
    private List<String> quizQuestions;
    private Context mContext;

    public QuizRecycleViewAdapter(Context c, List<String> quiz) {
        this.mContext = c;
        this.quizQuestions = quiz;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Style for the grid
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_quiz_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // User the position to get the profile to show a card
        // TODO downloads the picture using pets.get().getPhotoUrl
        String question = quizQuestions.get(position);
        holder.quiz_question.setText(question);
    }

    @Override
    public int getItemCount() {
        return quizQuestions.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView quiz_question;
        public MyViewHolder(View itemView) {
            super(itemView);

            quiz_question = itemView.findViewById(R.id.quiz_question);
        }
    }

}
