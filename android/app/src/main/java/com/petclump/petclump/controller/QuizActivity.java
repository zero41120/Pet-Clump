package com.petclump.petclump.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.petclump.petclump.R;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.QuizQuestion;
import com.petclump.petclump.models.protocols.ProfileUploader;
import com.petclump.petclump.views.QuizRecycleViewAdapter;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.List;

/**
 * This is the functionality for the quiz portion of the application, it returns a string of numbers that will serve as the quiz result. They key is "QuizResult"
 * also passed integer number of total questions to be/have been answered with key "questions"
 * ex) first time is passed 10, second time passed 20 ect
 */
public class QuizActivity extends AppCompatActivity implements ProfileUploader {

    //declarations
    private static final String TAG = "QuizActivity";
    private float x1, x2, y1, y2;
    private int index = -1;
    private String answers;
    private List<String> listOfQuestions;
    final int MIN_DISTANCE = 150;
    private Context c;
    private PetProfile profile;
    private String petId;
    private CardStackView cardStackView;
    Integer sequence = 0;
    TextView quizview;

    private Boolean isQuizReady() {
        // When download complete, index will be set to 0
        Log.d(TAG, "isQuizReady: " + index);
        if (index == 10) {
            profile.setQuiz(answers);
            profile.upload(petId, this);// Finish doesn't work, I don't know why.
            quizview.setVisibility(View.VISIBLE);
            cardStackView.setVisibility(View.GONE);
            quizview.setOnClickListener(v->finish());

        }

        return index > -1 && index < 10;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        quizview = (TextView) findViewById(R.id.quizView);
        cardStackView = (CardStackView) findViewById(R.id.quiz_card_stack_view);
        quizview.setVisibility(View.GONE);
        c = getApplicationContext();
        index = -1;
        sequence = getIntent().getIntExtra("sequence", 0);
        petId = FirebaseAuth.getInstance().getUid() + sequence;
        profile = new PetProfile() {{
            download(petId, () -> {
                listOfQuestions = QuizQuestion.getQuestion(c, profile.getQuiz(), 10);
                answers = profile.getQuiz();
                Log.d(TAG, "instance initializer: " + answers);
                index = 0;
                setUpChatview();
            });
        }};



    }
    public void setUpChatview(){

        QuizRecycleViewAdapter quizRecycleViewAdapter = new QuizRecycleViewAdapter(this);
        quizRecycleViewAdapter.addAll(listOfQuestions);
        //cardStackView = (CardStackView) findViewById(R.id.quiz_card_stack_view);
        cardStackView.setAdapter(quizRecycleViewAdapter);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString());
                Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());
                if (cardStackView.getTopIndex() == quizRecycleViewAdapter.getCount() - 5) {
                    Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
                }
                if (direction.toString()=="Right"){
                    answers += QuizQuestion.YES;
                    index += 1;
                    Log.d(TAG, "right");
                }else if (direction.toString()=="Left"){
                    answers += QuizQuestion.NO;
                    index += 1;
                    Log.d(TAG, "left");
                }
                else if (direction.toString()=="Top"){
                    answers += QuizQuestion.SKIP;
                    index += 1;
                }
                if (!isQuizReady()){
                    return;
                };
            }

            @Override
            public void onCardReversed() {
                Log.d("CardStackView", "onCardReversed");
            }

            @Override
            public void onCardMovedToOrigin() {
                Log.d("CardStackView", "onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                Log.d("CardStackView", "onCardClicked: " + index);
            }
        });
    }

    @Override
    public void didCompleteUpload() {

    }

//    //function listens for screen touches and does things based on gesture
//    public boolean onTouchEvent(MotionEvent event) {
//        if (!isQuizReady()) {
//            return super.onTouchEvent(event);
//        }
//        TextView viewQuiz = findViewById(R.id.quizView);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                x1 = event.getX();
//                y1 = event.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = event.getX();
//                y2 = event.getY();
//                float deltaX = x2 - x1, deltaY = y2 - y1;
//
//                if (Math.abs(deltaX) > MIN_DISTANCE & Math.abs(deltaY) < MIN_DISTANCE) {
//                    // Right
//                    if (x2 > x1) {
//                        Log.d(TAG, "right");
//                        answers += QuizQuestion.YES;
//                        index += 1;
//                    }
//                    // Left
//                    else {
//                        Log.d(TAG, "left");
//                        answers += QuizQuestion.NO;
//                        index += 1;
//                    }
//                } else {
//                    // UP
//                    if (Math.abs(deltaY) > MIN_DISTANCE & Math.abs(deltaX) < MIN_DISTANCE) {
//                        if (y1 > y2) {
//                            answers += QuizQuestion.SKIP;
//                            index += 1;
//                        }
//                    }
//                }
//                if (index == 10) {
//                    viewQuiz.setText(R.string.All_done);
//                    break;
//                } else {
//                    viewQuiz.setText(listOfQuestions.get(index));
//                }
//        }
//        return super.onTouchEvent(event);
//    }
}
