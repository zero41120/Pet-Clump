package com.petclump.petclump;

import android.app.Activity;
import android.content.Intent;
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
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petclump.petclump.models.OwnerProfile;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
This is the functionality for the quiz portion of the application, it returns a string of numbers that will serve as the quiz result. They key is "QuizResult"

also passed integer number of total questions to be/have been answered with key "questions"

ex) first time is passed 10, second time passed 20 ect

*/

public class QuizActivity extends AppCompatActivity {

    //populate pet questions
    public static List<String> PetQuestions(){
        List<String> petQuestions =new ArrayList<String>();

        petQuestions.add("Does your pet attack other pets");
        petQuestions.add("Does your pet wear costumes");
        petQuestions.add("Does your pet love to listen to music");
        petQuestions.add("Does your pet love can food");
        petQuestions.add("Does your pet chase other people around");
        petQuestions.add("Does your pet recognize its name when you call it");
        petQuestions.add("Can your pet be put into a jar");
        petQuestions.add("Does your pet love to take a bath");
        petQuestions.add("Does your pet bark/meow");
        petQuestions.add("Does your pet get along pretty well with other pets");
        petQuestions.add("Does your pet run faster than you");
        petQuestions.add("Does your pet love watching TV");
        petQuestions.add("Does your pet love surfing");
        petQuestions.add("Does your pet love the beach");
        petQuestions.add("Does your pet love sunshine");
        petQuestions.add("Does your pet love jungle");
        petQuestions.add("Does your pet love swimming");
        petQuestions.add("Does your pet love flowers");
        petQuestions.add("Have your pet done a surgery before");
        petQuestions.add("Does your pet love to go to a park");
        petQuestions.add("Does your pet love money");
        petQuestions.add("Does your pet randomly poo");
        petQuestions.add("Is your pet smart");
        petQuestions.add("Is your pet long hair");
        petQuestions.add("Is your pet nearsighted");
        petQuestions.add("Is your pet well-mannered");
        petQuestions.add("Is your pet introvert or extrovert");
        petQuestions.add("Is your pet pregnant");
        petQuestions.add("Have your pet mated before");
        petQuestions.add("Does your pet has a dream");
        petQuestions.add("Have your pet ever sleepwalked");
        petQuestions.add("Does your pet smoke weeds");
        petQuestions.add("Does your pet ever want to eat you");
        petQuestions.add("Is your pet afraid of you");

        return petQuestions;
    }

    //Populate user questions
    public static List<String> UserQuestions(){
        List<String> questions = new ArrayList<String>();

        questions.add("Do you like long-hair animals? ");
        questions.add("Do you like short-hair animals?");
        questions.add("Do you talk to your pet everyday? ");
        questions.add("Do you sleep with your pet next to you?");
        questions.add("Do you cuddle with your pet?");
        questions.add("Do you feel energized when your pet is around you?");
        questions.add("Do you think about your pet when they are not with you?");
        questions.add("Do you sing to your pet?");
        questions.add("Do you always feed your pet on time?");
        questions.add("Have you ever wished that you and your pet can switch identity?");
        questions.add("Do you tickle your pet");
        questions.add("If your pet turns into human one day, would you want to date them");
        questions.add("Do you say goodnight to your pet before you go to sleep");
        questions.add("Do you take your pet outside");
        questions.add("Do you like to answer questions");
        questions.add("Do you think that your pet is cute");
        questions.add("Do you pet different species of animals");
        questions.add("Are you a vegan");
        questions.add("Do you kiss your pet");
        questions.add("Do you love your pet");
        questions.add("Do you buy high quality food to your pet");
        questions.add("Do you beat your pet");
        questions.add("Do you take your pet to your office or school");
        questions.add("Do you train your pet to do interesting activities");
        questions.add("Can you bear that when your pet bothers you");
        questions.add("Will you be distracted by your pet because your pets are too lovely");
        questions.add("Can you bear your pet to date with other pets and make you lonely");
        questions.add("Do you walk with your pet on leash");
        questions.add("Can your salary easily afford your pet");
        questions.add("Do you want to buy more pets");
        questions.add("Do you think sterilization is fair for pets");
        questions.add("Do you prefer big pets or small pets");
        questions.add("Do you think your pet gradually looks similar to you");
        questions.add("Do you think your pet regard you as a servant");
        questions.add("Do you spend most of your money for your pet");
        questions.add("Does your pet make you angry sometimes");

        return questions;
    }

    //combines questions from pets and users
    private List<String> Questions() {
        List<String> combinedQuestions = new ArrayList<String>();
        int i;
        int j;
        j = getIntent().getExtras().getInt("questions")/2;
        i = j-5;
        for(i = 0; i < 5; i++) {
            combinedQuestions.add(PetQuestions().get(i));
            combinedQuestions.add(UserQuestions().get(i));
        }
        return combinedQuestions;
    }


//declarations
    private float x1,x2,y1,y2;
    private int index = 0;
    private int[] answers = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final List<String> ListOfQuestions= Questions();
    final int MIN_DISTANCE = 150;
    private Context c;
    int result = 0;






//function listens for screen touches and does things based on gesture
    public boolean onTouchEvent(MotionEvent event)
    {
        TextView viewQuiz = findViewById(R.id.quizView);
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

                            //Toast.makeText(this, "Left to Right swipe" + Arrays.toString(answers), Toast.LENGTH_SHORT).show();

                            if (index >= answers.length) {
                                for (int i = 0; i < answers.length; i++) {
                                    result = result * 10 + answers[i];
                                }
                                Intent intent = new Intent();
                                intent.putExtra("QuizResult", result);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }

                            viewQuiz.setText(ListOfQuestions.get(index));

                            break;
                        } else {
                            //left swipe
                            answers[index] = 0;
                            index = index + 1;

                            //Toast.makeText(this, "Right to Left swipe" + Arrays.toString(answers), Toast.LENGTH_SHORT).show();

                            if (index >= answers.length) {
                                for (int i = 0; i < answers.length; i++) {
                                    result = result * 10 + answers[i];
                                }
                                Intent intent = new Intent();
                                intent.putExtra("QuizResult", result);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }

                            viewQuiz.setText(ListOfQuestions.get(index));

                            break;
                        }

                    } else {
                        if (Math.abs(deltaY) > MIN_DISTANCE & Math.abs(deltaX) < MIN_DISTANCE) {
                            if (y1 > y2) {
                                //up swipe
                                answers[index] = 2;
                                index = index + 1;

                                //Toast.makeText(this, "Bottom to Top Swipe" + Arrays.toString(answers), Toast.LENGTH_SHORT).show();

                                if (index >= answers.length) {
                                    for (int i = 0; i < answers.length; i++) {
                                        result = result * 10 + answers[i];
                                    }
                                    Intent intent = new Intent();
                                    intent.putExtra("QuizResult", result);

                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }

                                viewQuiz.setText(ListOfQuestions.get(index));

                                break;
                            }
                        }
                    }
                    break;
            }
            return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        TextView quizView = (TextView)findViewById(R.id.quizView);
        quizView.setText(ListOfQuestions.get(0));
    }
}
