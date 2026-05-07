package com.example.secureapp.service;

import com.example.secureapp.model.User;
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
public class CustomUserDetailsService implements UserDetailsService {
    private final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public void registerUser(String username, String password, String role,
                             String firstName, String lastName, String email) throws Exception {
        // Validation
        if (firstName == null || firstName.isBlank())
            throw new Exception("First name is required.");
        if (lastName == null || lastName.isBlank())
            throw new Exception("Last name is required.");
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches())
            throw new Exception("A valid email address is required.");
        if (users.containsKey(username))
            throw new Exception("Username '" + username + "' is already taken.");

        String encodedPassword = passwordEncoder.encode(password);
        users.put(username, new User(username, encodedPassword, role, firstName, lastName, email));
    }

    public User getUser(String username) {
        return users.get(username);
    }
}
