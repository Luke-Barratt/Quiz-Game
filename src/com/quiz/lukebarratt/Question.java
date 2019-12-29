package com.quiz.lukebarratt;

import java.util.TreeMap;

public class Question {

    private int questionID;
    private int answerID;
    private String text;

    public int getQuestionID() {
        return questionID;
    }

    public int getAnswerID() {
        return answerID;
    }

    public String getText() {
        return text;
    }

    private TreeMap<Integer, String> options;

    public TreeMap<Integer, String> getOptions() {
        return options;
    }

    public Question(int questionID, int answerID, String text) {
        this.questionID = questionID;
        this.answerID = answerID;
        this.text = text;

        options = new TreeMap<>();
    }

    public void addOption(int id, String option) {
        options.put(id, option);
    }

}
