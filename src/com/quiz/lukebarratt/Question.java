package com.quiz.lukebarratt;

import java.util.TreeMap;

public class Question {

    //Declare variable to hold values from xml file.
    private int questionID;
    private int answerID;
    private String text;

    //Getter for questionID.
    public int getQuestionID() {
        return questionID;
    }

    //Getter for answerID.
    public int getAnswerID() {
        return answerID;
    }

    //Getter for text
    public String getText() {
        return text;
    }

    //Declaring a Tree map to store options and option id for each question.
    private TreeMap<Integer, String> options;

    //Getter to retrieve question options and option id stored in options tree map.
    public TreeMap<Integer, String> getOptions() {
        return options;
    }

    //Constructor for Question class.
    public Question(int questionID, int answerID, String text) {
        this.questionID = questionID;
        this.answerID = answerID;
        this.text = text;

        //Create new Tree map called to store question options
        options = new TreeMap<>();
    }

    //Method to add option id and option to tree map declared above.
    public void addOption(int id, String option) {
        options.put(id, option);
    }

}
