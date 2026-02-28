package com.RestAPI_LAB.library;

import com.RestAPI_LAB.library.model.Book;
import com.RestAPI_LAB.library.service.LibraryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LibraryController {
    // create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @Autowired
    private LibraryService libraryService;

    // ------------- Book endpoints -------------

    // Get all books
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = libraryService.getAllBooks();
        logger.info("The list of book returned "+books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Get a book by id
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = libraryService.getBookByID(id);
        logger.info("The book returned "+book);
        return book.map(value -> new ResponseEntity<>(value,HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        // return ResponseEntity.of(book); -> it automatically returns 200 OK or 404 not found
    }

    // add a new book
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        libraryService.addBook(book);
        logger.info("The book was added");
        return new ResponseEntity<>(book,HttpStatus.CREATED);
    }


}
