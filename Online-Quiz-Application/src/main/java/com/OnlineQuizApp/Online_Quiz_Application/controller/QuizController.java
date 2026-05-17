package com.OnlineQuizApp.Online_Quiz_Application.controller;

import com.OnlineQuizApp.Online_Quiz_Application.model.Question;
import com.OnlineQuizApp.Online_Quiz_Application.model.User;
import com.OnlineQuizApp.Online_Quiz_Application.service.QuestionsService;
import com.OnlineQuizApp.Online_Quiz_Application.service.QuizUserDetailsService;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.desktop.QuitResponse;
import java.util.List;

/**GET request for retrieving the login page
 GET request for retrieving the regitration page
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
    @GetMapping("/registration")
    public String register() {
        return  "register"; // return the register/html template
    }




}
