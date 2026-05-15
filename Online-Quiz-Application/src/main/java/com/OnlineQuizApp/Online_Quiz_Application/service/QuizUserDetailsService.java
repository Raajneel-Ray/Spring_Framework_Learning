package com.OnlineQuizApp.Online_Quiz_Application.service;

import com.OnlineQuizApp.Online_Quiz_Application.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    private final Map<String, User> users = new HashMap<>();


}
