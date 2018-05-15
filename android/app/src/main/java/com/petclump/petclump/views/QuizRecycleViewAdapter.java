package com.petclump.petclump.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.petclump.petclump.R;

import java.util.List;
//Thanks to the library provider! https://github.com/yuyakaido/CardStackView
public class QuizRecycleViewAdapter extends ArrayAdapter<String> {
    private List<String> quizQuestions;
    private Context mContext;

    public QuizRecycleViewAdapter(Context c) {
        super(c, 0);
    }
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_quiz_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        String question = getItem(position);

        holder.question.setText(question);

        return contentView;
    }

    private static class ViewHolder {
        public TextView question;


        public ViewHolder(View view) {
            this.question = view.findViewById(R.id.quiz_question);
        }
    }



}
