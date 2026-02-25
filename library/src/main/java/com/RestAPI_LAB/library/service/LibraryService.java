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

import java.util.ArrayList;
import java.util.List;
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


}
