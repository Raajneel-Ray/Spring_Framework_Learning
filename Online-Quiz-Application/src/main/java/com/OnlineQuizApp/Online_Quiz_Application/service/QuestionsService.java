package com.OnlineQuizApp.Online_Quiz_Application.service;

import com.OnlineQuizApp.Online_Quiz_Application.model.Question;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class QuestionsService {
    // implementing the question service app, it will return the list of questions, add, edit and delete questions
    // it uses a hashmap to store questions in heap memory

    private final Map<Integer, Question> questions = new HashMap<>();
    private int nextId = 1;  // this acts as manual primary key generator

    public ArrayList<Question> getQuestionsList() {
        ArrayList<Question> values = new ArrayList<>(questions.values());
        return values;
    }

    public Question getQuestionById(int id) {
        return questions.get(Integer.valueOf(id));
    }

    public int getNextId() {
        return nextId++;
    }

    public boolean addQuestion(Question question) {
        Integer questionId = question.getId();
        if(questionId == 0) {
            // assign current nextID and increment it for the next question
            question.setId(nextId++);
        }
        if(questions.containsKey(questionId)) {
            return false;
        }
        else {
            questions.put(questionId, question);
            return true;
        }
    }

    public boolean editQuestion(Question question) {
        Integer questionId = question.getId();
        if(questions.containsKey(questionId)) {
            questions.put(questionId, question);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteQuestion(Integer questionId) {
        Integer id = Integer.valueOf(questionId);
        if(questions.containsKey(id)) {
            questions.remove(id);
            return true;
        }
        else {
            return false;
        }
    }

    public int submitQuestion(ArrayList<Question> list) {
        int result = 0;
        for(Question question : list) {
            // Finding the matching real question from the hashmap
            Question questionInList = questions.get(question.getId());

            // Comparing it with the real answer to user's submitted answer here question.getCorrectAnswer() refers to the answer selected by user
            if(questionInList.getCorrectAnswer().equals(question.getCorrectAnswer())) {
                result++;
            }
        }
        return result;
    }


}
