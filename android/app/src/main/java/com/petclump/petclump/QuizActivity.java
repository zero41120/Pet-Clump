package com.petclump.petclump;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
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

    //Quiz
    private static void Quiz(){
        //initialize lists
        char[] answers = new char[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        List<String> ListOfQuestions= QuizQuestions();



        for(int i=0; i<= 10; i++){
            String Question = ListOfQuestions.get(i);

            //updste current question to textview
            //initialize buttons or swipes (yes, no, skip)

            //on yes press
            answers[i] = 1;
            continue;

            //on no press
            answers[i] = 2;
            continue;

            //on skip
            continue;




        }
        //push "answers" to database

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
    }
}
