package com.OnlineQuizApp.Online_Quiz_Application.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Question {
    private int id;
    private String questionText;
    private ArrayList<String> options;
    private String correctAnswer;

    public Question() {
        this.options = new ArrayList<>();
    }
    public Question(int id, String questionText, ArrayList<String> options, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    // method to get options as comma separated
    public String getOptionsAsString() {
        return String.join(",", options);
    }

    // method to set options from a comma separated string
    public void setOptionsFromString(String optionsString) {
        this.options = new ArrayList<>(Arrays.asList(optionsString.split(",")));
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    @Override
    public String toString() {
        return "Question [id=" + id + ", text=" + questionText + ", options=" + options + "]";
    }
}
