package com.petclump.petclump.models;

public class Quiz {
    String question;
    Boolean answer;
    QuizType type;
}

enum QuizType{
    OWNER, PET
}