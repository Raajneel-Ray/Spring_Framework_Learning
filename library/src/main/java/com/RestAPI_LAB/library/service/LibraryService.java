/*Books
Get all books
Get a book by ID
Add a new book
Update a book
Delete a book by ID
Members
Get all members
Get a member by ID
Add a new member
Update a member
Delete a member by ID
BorrowingRecords
Get all borrowing records
Borrow a book (create a new borrowing record)
Return a book (update the borrowing record with the return date)*/

package com.RestAPI_LAB.library.service;

import com.RestAPI_LAB.library.model.Book;
import com.RestAPI_LAB.library.model.BorrowingRecord;
import com.RestAPI_LAB.library.model.Member;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LibraryService {
    // in memory storage using ArrayList
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();

    // Book Methods
    public List<Book> getAllBooks() {
        return books;
    }
    // Get a book by id
    public Optional<Book> getBookByID(Long id) {
        // Optional is a container object , if a book is found it contains that book, if no books matches it returns Optional.empty() instead of null
        /*Why use Optional?
         It forces the "caller" of this method to acknowledge that the book might not exist. Instead of risking a NullPointerException,
         the developer using your method must handle the empty case explicitly:*/
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    // Add a new book
    public void addBook(Book book) {
        books.add(book);
    }

    // Update a book
    public void updateBook(Book updatedBook) {
        for(int i=0 ;i< books.size(); i++) {
            Book book = books.get(i);
            if(book.getId().equals(updatedBook.getId())) {
                books.set(i,updatedBook);
                break;
            }
        }
    }

    // delete a book
    public void deleteBook(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    // search book by title
    public List<Book> getBookByTitle(String title) {
        if(title == null || title.trim().isEmpty()) {
            return  new ArrayList<>();
        }

        List<Book> matchingBooks = new ArrayList<>();
        for(int i=0; i<books.size(); i++) {
            Book book = books.get(i);
            if(book.getTitle() != null && book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }

    // search a book by author
    public List<Book> getBookByAuthor(String author) {
        if(author == null || author.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Book> matchingBooks = new ArrayList<>();
        for(Book book : books) {
            if(book.getAuthor() != null && book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }

    // search books by genre
    public List<Book> getBookByGenre(String genre) {
        if(genre == null || genre.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return books.stream()
                .filter(book -> book.getGenre()!=null && book.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .toList();
    }

    // Search a book by author, optionally filtered by genre
    public List<Book> getBookByAuthorAndGenre(String author, String genre) {
        if(author == null || author.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author)) // filter by author
                .filter(book -> genre == null || genre.isBlank() || (book.getGenre().toLowerCase().contains(genre.toLowerCase()))) // // Optional filter by genre
                .toList();
    }

    // getting books due on date
    public List<Book> getBooksDueOnDate(LocalDate dueDate) {
        if(dueDate == null) {
            return new ArrayList<>();
        }
        return borrowingRecords.stream()
                .filter(record -> record.getDueDate() != null && record.getDueDate().isEqual(dueDate))
                .map(record -> getBookByID(record.getBookId()).orElse(null)) // extract books directly
                .filter(Objects :: nonNull)
                .toList();
    }

    // method for getting the earliest date on which a book is available
    public LocalDate checkAvailability(Long bookId) {
        if(bookId == null) {
            return null;
        }

        // find the book first
        Optional<Book> bookToCheck = books.stream()
                .filter(book -> Objects.equals(book.getId(),bookId))
                .findFirst();
        if(bookToCheck.isEmpty()) {
            return null; //book not found
        }
        //It extracts the actual Book object from the Optional.
        Book book = bookToCheck.get();
        // if copies available return today
        if(book.getAvailableCopies() > 0) {
            return LocalDate.now();
        }
        return borrowingRecords.stream()
                .filter(record -> Objects.equals(record.getBookId(),bookId)
                        && record.getReturnDate() == null && record.getDueDate() != null)
                .map(BorrowingRecord :: getDueDate)
                .min(LocalDate::compareTo)
                .orElse(null);
    }
    // ---------------- Member Methods --------------------

    // Get All Members
    public List<Member> getAllMembers() {
        return members;
    }

    // Get a member by ID
    public Optional<Member> getMemberByID(Long id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    // Add a new Member
    public void addMember(Member member) {
        members.add(member);
    }

    // Update a Member
    public void updateMember(Member updatedMember) {
        for(int i=0; i<members.size(); i++) {
            Member member = members.get(i);
            if(member.getId().equals(updatedMember.getId())) {
                members.set(i,updatedMember);
                break;
            }
        }
    }

    // Delete a member by ID
    public void deleteMember(Long id) {
        members.removeIf(member -> member.getId().equals(id));
    }

    // --------------- BorrowingRecord Methods ---------------
    // get all borrowing record
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecords;
    }

    // borrow a book (create a new borrowing record)
    public void borrowBook(BorrowingRecord record) {
        // it prevents from entering null values in the record
        if (record.getId() == null ||
                record.getBookId() == null ||
                record.getMemberId() == null) {
            throw new IllegalArgumentException("Invalid borrowing record");
        }
        // Find the book
        Optional<Book> bookOpt = getBookByID(record.getBookId());
        if(bookOpt.isEmpty()){
            throw new RuntimeException("Book not found.");
        }
        Book book = bookOpt.get();
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available");
        }
        // Set borrow date and due date (e.g., due date = borrow date + 14 days)
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        borrowingRecords.add(record);

        // decreasing the available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
    }

    // return the book -- update the borrowing record with the return date
    public void returnBook(Long recordID, LocalDate returnDate) {
        for(BorrowingRecord record: borrowingRecords) {
            if(record.getId().equals(recordID)) {
                record.setReturnDate(returnDate);
                Optional<Book> bookOpt = getBookByID(record.getBookId());
                // Increase the available copies of the book
                if(bookOpt.isPresent()){
                    Book book = bookOpt.get();
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                }
                break;
            }
        }
    }
}
