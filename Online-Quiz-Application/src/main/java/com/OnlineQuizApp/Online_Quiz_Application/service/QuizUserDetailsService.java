package com.OnlineQuizApp.Online_Quiz_Application.service;

import com.OnlineQuizApp.Online_Quiz_Application.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    // built-in spring security interface indicating to user this class to authenticate users.
    private final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with username :" + username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public void registerUser(String username, String password, String email, String role) throws Exception {
        if(users.containsKey(username)) {
            throw new Exception("Username '" + username + "' is already taken.");
        }
        if(email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new Exception("A valid username is required.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        users.put(username, new User(username, encodedPassword, email, role));
    }

    public User getUser(String username) {
        return users.get(username);
    }


}
