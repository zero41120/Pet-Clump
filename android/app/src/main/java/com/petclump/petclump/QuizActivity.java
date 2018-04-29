package com.petclump.petclump;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    //Quiz questions
    public static List<String> QuizQuestions(){
        List<String> questions = new ArrayList<String>();

        questions.add("Do you like long-hair animal? ");
        questions.add("Do you like short-hair animal?");
        questions.add("Do you talk to your pet everyday? ");
        questions.add("Do you sleep with your pet next to you?");
        questions.add("Do you cuddle with your pet?");
        questions.add("Do you feel energized when your pet is around you?");
        questions.add("Do you think about your pet when they are not with you?");
        questions.add("Do you sing to your pet?");
        questions.add("Do you always feed your pet on time?");
        questions.add("Have you ever wished that you and your pet can switch identity? ");

        return questions;
    }



    private float x1,x2,y1,y2;
    private int index = 0;
    private int[] answers = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final List<String> ListOfQuestions= QuizQuestions();
    final int MIN_DISTANCE = 150;
    //View myView = findViewById(R.id.quzView);
    //TextView quizView = (TextView)findViewById(R.id.quzView);



    public boolean onTouchEvent(MotionEvent event)
    {
        String Question = ListOfQuestions.get(index);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    y2 = event.getY();

                    float deltaX = x2 - x1;
                    float deltaY = y2 - y1;

                    if (Math.abs(deltaX) > MIN_DISTANCE & Math.abs(deltaY) < MIN_DISTANCE) {
                        if (x2 > x1) {
                            //right swipe
                            answers[index] = 1;
                            index = index + 1;
                            Toast.makeText(this, "Left to Right swipe" + Arrays.toString(answers) + index, Toast.LENGTH_SHORT).show ();
                            break;
                     //       continue;
                        } else {
                            //left swipe
                            answers[index] = 0;
                            index = index + 1;
                            Toast.makeText(this, "Right to Left swipe" + Arrays.toString(answers) + index, Toast.LENGTH_SHORT).show ();
                            break;
                         //   continue;
                        }

                    } else {
                        if (Math.abs(deltaY) > MIN_DISTANCE & Math.abs(deltaX) < MIN_DISTANCE) {
                            if (y1 > y2) {
                                //up swipe
                                Toast.makeText(this, "Bottom to Top Swipe", Toast.LENGTH_SHORT).show ();
                                answers[index] = 2;
                                index = index + 1;
                                break;
                             //   continue;
                            }
                        }
                    }
                    break;
            }
   //     }
            return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
    }
}
