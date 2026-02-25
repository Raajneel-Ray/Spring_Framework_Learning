package com.RestAPI_LAB.library;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {
    @GetMapping("/")
    public String listBooks() {
        return "<h1>Here are the list of Books</h1>";
    }
}
