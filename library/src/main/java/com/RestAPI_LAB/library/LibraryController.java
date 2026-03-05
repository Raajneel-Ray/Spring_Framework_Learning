package com.RestAPI_LAB.library;

import com.RestAPI_LAB.library.model.Book;
import com.RestAPI_LAB.library.model.BorrowingRecord;
import com.RestAPI_LAB.library.model.Member;
import com.RestAPI_LAB.library.service.LibraryService;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
//    @GetMapping("/books")
//    public ResponseEntity<List<Book>> getAllBooks() {
//        List<Book> books = libraryService.getAllBooks();
//        logger.info("The list of book returned "+books);
//        return new ResponseEntity<>(books, HttpStatus.OK);
//    }

    // Modifying the above endpoint to take optional request parameter for both author and genre and combine the 3 endpoints to ons
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {
        if(author!=null && genre!=null) {
            List<Book> books = libraryService.getBookByAuthorAndGenre(author, genre);
            logger.info("The book retrieved for the author and genre " +author+ "-" +genre);
            return new ResponseEntity<>(books,HttpStatus.OK);
        } else if (author!=null) {
            List<Book> books = libraryService.getBookByAuthorAndGenre(author,null);
            logger.info("The books retrieved for the author "+author);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } else {
            List<Book> books = libraryService.getAllBooks();
            logger.info("All the books retrieved");
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
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

    // Update a book
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        if(!libraryService.getBookByID(id).isPresent()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        updatedBook.setId(id);
        libraryService.updateBook(updatedBook);
        logger.info("The book has been updated "+updatedBook);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    // Delete a book
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if(!libraryService.getBookByID(id).isPresent()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        libraryService.deleteBook(id);
        logger.info("The book has been deleted.");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search a book by title
    @GetMapping("/books/search/{searchQuery}")
    public ResponseEntity<List<Book>> getBookByTitle(@PathVariable String searchQuery) {
        List<Book> books = libraryService.getBookByTitle(searchQuery);
        logger.info("The books retrieved for title "+searchQuery);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Search a book by author
    @GetMapping("/books/search/author/{searchQuery}")
    public ResponseEntity<List<Book>> getBookByAuthor(@PathVariable String searchQuery) {
        List<Book> books = libraryService.getBookByAuthor(searchQuery);
        logger.info("The books retrieved for author "+searchQuery);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // search a book by genre
    @GetMapping("/books/genre")
    public ResponseEntity<List<Book>> getBookByGenre(@RequestParam String genre) {
        List<Book> books = libraryService.getBookByGenre(genre);
        logger.info("The books retrieved for genre "+genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // search books by author, optionally filtered by genre
    @GetMapping("/books/author")
    public ResponseEntity<List<Book>> getBooksByAuthorAndGenre(@RequestParam String author,
                                                               @RequestParam(required = false) String genre) {
        List<Book> books = libraryService.getBookByAuthorAndGenre(author, genre);
        logger.info("The books retrieved for the author and genre "+author+" - " + genre);
        return ResponseEntity.ok(books); // different format it do not need the new keyword
    }

    // search books due on a date
    @GetMapping("/books/dueondate")
    public ResponseEntity<List<Book>> getBooksDueOnDate(@RequestParam("dueDate")
                                                        @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dueDate) {
        List<Book> books = libraryService.getBooksDueOnDate(dueDate);
        logger.info("The books retrieved by due date "+dueDate);
        return ResponseEntity.ok(books);
    }

    // get the earliest date a book will be available
    @GetMapping("/bookavailabileDate")
    public ResponseEntity<LocalDate> checkAvailability(@RequestParam Long bookId) {
        LocalDate avlDate = libraryService.checkAvailability(bookId);
        if(avlDate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return ResponseEntity.ok(avlDate);
        }
    }

    // ----------------------- Member Endpoints ------------------

    // Get all members
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = libraryService.getAllMembers();
        logger.info("The members in the system " + members);
        return new ResponseEntity<>(members,HttpStatus.OK);
    }

    // Get a member by id
    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = libraryService.getMemberByID(id);
        logger.info("The member you retrieved "+member);
        return member.map(value -> new ResponseEntity<>(value,HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Add a new member
    @PostMapping("/members")
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        libraryService.addMember(member);
        logger.info("The member has been added");
        return new ResponseEntity<>(member,HttpStatus.CREATED);
    }

    // Update a member
    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id,@RequestBody Member updatedMember) {
        if(!libraryService.getMemberByID(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedMember.setId(id);
        libraryService.updateMember(updatedMember);
        logger.info("The member has been updated "+ updatedMember);
        return new ResponseEntity<>(updatedMember,HttpStatus.OK);
    }

    // Delete a member
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (!libraryService.getMemberByID(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        libraryService.deleteMember(id);
        logger.info("The member has been deleted "+id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // --------------- Borrowing Records endpoints ------------------

    // Get all borrowing records
    @GetMapping("/borrowing-records")
    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords() {
        List<BorrowingRecord> records = libraryService.getAllBorrowingRecords();
        logger.info("The records has been retrieved "+records);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<BorrowingRecord> borrowBook(@RequestBody BorrowingRecord record) {
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        libraryService.borrowBook(record);
        logger.info("The book has been borrowed "+record);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    // Return a book
    @PutMapping("/return/{recordId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long recordId, HttpServletRequest httpServletRequest) {
        libraryService.returnBook(recordId, LocalDate.now());
        logger.info("The book has been retrieved "+ recordId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
