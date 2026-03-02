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
    public ArrayList<Book> getBookByTitle(String title) {
        if(title == null || title.trim().isEmpty()) {
            return  new ArrayList<>();
        }

        ArrayList<Book> matchingBooks = new ArrayList<>();
        for(int i=0; i<books.size(); i++) {
            Book book = books.get(i);
            if(book.getTitle() != null && book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
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
        // Set borrow date and due date (e.g., due date = borrow date + 14 days)
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        borrowingRecords.add(record);

        // decreasing the available copies
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() - 1);
    }

    // return the book -- update the borrowing record with the return date
    public void returnBook(Long recordID, LocalDate returnDate) {
        for(BorrowingRecord record: borrowingRecords) {
            if(record.getId().equals(recordID)) {
                record.setReturnDate(returnDate);
                Book book = record.getBook();
                // Increase the available copies of the book
                book.setAvailableCopies(book.getAvailableCopies()+1);
                break;
            }
        }
    }
}
