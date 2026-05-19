package com.OnlineQuizApp.Online_Quiz_Application.controller;

import com.OnlineQuizApp.Online_Quiz_Application.model.Question;
import com.OnlineQuizApp.Online_Quiz_Application.model.User;
import com.OnlineQuizApp.Online_Quiz_Application.service.QuestionsService;
import com.OnlineQuizApp.Online_Quiz_Application.service.QuizUserDetailsService;
import org.apache.coyote.Request;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.awt.desktop.QuitResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**GET request for retrieving the login page
 GET request for retrieving the registration page
 POST request for registering user
 GET mapping for the retrieving the addQuiz page
 POST request for adding quiz questions
 GET mapping for the retrieving the editQuiz page
 PUT request for editing quiz questions
 DELETE request for removing quiz questions
 GET request for retrieving quiz questions (home page - different for admin and regular users)
 POST request for submitting answers
 GET request for retrieving the result page */
@Controller
public class QuizController {

    private final QuestionsService questionService;
    private final QuizUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public QuizController(QuizUserDetailsService userDetailsService, AuthenticationManager authenticationManager, QuestionsService questionService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.questionService = questionService;
    }
    @GetMapping("/home")
    public String homepage(Model model) {
        // get authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        // get the username
        String username = authentication.getName();
        model.addAttribute("username", username);

        User user = userDetailsService.getUser(username);
        model.addAttribute("username", username);

        // getting the user's role from authentication
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER"); // default role if no authority is found

        if(role.equals("ROLE_ADMIN")) {
            // fetch the latest questions available in service
            List<Question> quizzes = questionService.getQuestionsList();
            model.addAttribute("quizzes",quizzes);
            return "QuizList";  // return the QuizList.html template
        } else {
            List<Question> quizzes = questionService.getQuestionsList();
            model.addAttribute("quizzes", quizzes);
            return "Quiz"; // return the Quiz.html template where edit, delete and add option is not available
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // return the login.html template
    }
    @GetMapping("/register")
    public String register() {
        return  "register"; // return the register/html template
    }
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String role
                               ) {
        try {
            userDetailsService.registerUser(username,password,email,role);
        } catch (Exception userExistsAlready) {
            return "redirect:/register?error";
        }

        // authenticate the user in program by giving a token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return "redirect:/login?success";
    }
    @GetMapping("/addQuiz")
    public String showAddQuizForm(Model model) {
        model.addAttribute("quiz", new Question()); // add a new question object to the model
        return "addQuiz";
    }
    @PostMapping("/addQuiz")
    public String addQuiz(@ModelAttribute Question question, Model model, Authentication authentication) {
        // get the user's role
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");  // default role if no authority found

        // redirecting to appropriate page based on role
        if(role.equals("ROLE_ADMIN")) {
            question.setId(questionService.getNextId());
            questionService.addQuestion(question);
            model.addAttribute("success", "Quiz added successfully!");
            return "redirect:/home";  // redirect to quiz list page
        }
        else {
            model.addAttribute("error","You do not have permission to add a quiz");
            return "redirect:/addQuiz?error";
        }
    }

    @GetMapping("/editQuiz/{id}")
    public String showEditQuizForm(@PathVariable("id") int id, Model model) {
        // find the question by id
        Question quiz = questionService.getQuestionById(id);

        // add the question to the model
        model.addAttribute("quiz",quiz);
        return "editQuiz";
    }

    @PostMapping("/editQuestion")
    public String editQuestion(@ModelAttribute("quiz") Question question) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // get the users role
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER"); // default user role

        if(role.equals("ROLE_ADMIN")) {
            questionService.editQuestion(question);
            return "redirect:/home";
        }
        else {
            return "redirect:/home";
        }
    }

    @GetMapping("/deleteQuestion/{id}")
    public String deleteQuestion(@PathVariable("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // get the users role
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER"); // default user role
        if(role.equals("ROLE_ADMIN")) {
            questionService.deleteQuestion(id);
            return "redirect:/home";
        }
        else {
            return "redirect:/home";
        }
    }

    @PostMapping("/submitQuiz")
    public String evaluateQuiz(@RequestParam Map<String, String> allParams, Model model) {

        //original questions from hashmap
        ArrayList<Question> originalQuestions = questionService.getQuestionsList();

        // user submitted answers
        ArrayList<Question> submittedQuestions = new ArrayList<>();

        // build submitted questions list
        for(int i=0; i<originalQuestions.size(); i++) {
            Question originalQuestion = originalQuestions.get(i);
            String userAnswer = allParams.get("answer"+i);

            Question submittedQuestion = new Question();

            submittedQuestion.setId(originalQuestion.getId());

            submittedQuestion.setCorrectAnswer(userAnswer); // store selected answers

            submittedQuestions.add(submittedQuestion);
        }

        // call question service method
        int score = questionService.submitQuestion(submittedQuestions);

        model.addAttribute("score", score);
        model.addAttribute("totalQuestions", originalQuestions.size());
        return "result";
    }

    @GetMapping("/result")
    public String resultPage() {
        return "result";
    }



}
