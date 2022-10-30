package org.hse.eduplugin.controllers;

import java.util.List;
import org.hse.eduplugin.services.BooksService;
import org.hse.eduplugin.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksController {

    @Autowired
    BooksService booksService;

    /**
     * Get all books from the database
     *
     * @return the List of all books
     */
    @GetMapping("/books")
    private ResponseEntity<List<Book>> getAllBooks() {
        return booksService.getAllBooks();
    }

    /**
     * Get the book with specific id in the database
     *
     * @param id of the book
     * @return the specific book
     */
    @GetMapping("/books/id/{id}")
    private ResponseEntity<Book> getBook(@PathVariable("id") Long id) {
        return booksService.getBookById(id);
    }

    /**
     * Get books with specific title in the database
     *
     * @param title of books
     * @return specific books
     */
    @GetMapping("/books/title/{title}")
    private ResponseEntity<List<Book>> getBooksByTitle(@PathVariable("title") String title) {
        return booksService.getBooksByTitle(title);
    }

    /**
     * Get books with specific author in the database
     *
     * @param author of books
     * @return specific books
     */
    @GetMapping("/books/author/{author}")
    private ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable("author") String author) {
        return booksService.getBooksByAuthor(author);
    }

    /**
     * Get books with specific isbn in the database
     *
     * @param isbn of books
     * @return specific books
     */
    @GetMapping("/books/isbn/{isbn}")
    private ResponseEntity<List<Book>> getBooksByIsbn(@PathVariable("isbn") String isbn) {
        return booksService.getBooksByIsbn(isbn);
    }

    /**
     * Get books with specific printYear in the database
     *
     * @param printYear of books
     * @return specific books
     */
    @GetMapping("/books/printYear/{printYear}")
    private ResponseEntity<List<Book>> getBooksPrintYear(@PathVariable("printYear") Integer printYear) {
        return booksService.getBooksPrintYear(printYear);
    }

    /**
     * Get books with specific readAlready in the database
     *
     * @param readAlready of books
     * @return specific books
     */
    @GetMapping("/books/readAlready/{readAlready}")
    private ResponseEntity<List<Book>> getBooksByReadAlready(@PathVariable("readAlready") Boolean readAlready) {
        return booksService.getBooksByReadAlready(readAlready);
    }

    /**
     * Delete the  book from the database
     *
     * @param id of book that user want to delete
     * @return deleted book
     */
    @DeleteMapping("/books/id/{id}")
    private ResponseEntity<Book> deleteBook(@PathVariable("id") Long id) {
        return booksService.deleteBook(id);
    }

    /**
     * Add the new book to the database
     *
     * @param book that user want to add
     * @return the id of the recently added book
     */
    @PostMapping("/books")
    private ResponseEntity<Long> postBook(@RequestBody Book book) {
        return booksService.postBook(book);
    }

    /**
     * Add new books to the database
     *
     * @param books that user want to add
     */
    @PostMapping("/books/all")
    @ResponseStatus(HttpStatus.CREATED)
    private void postAllBook(@RequestBody List<Book> books) {
        booksService.postAllBook(books);
    }

    /**
     * Update the existing book in the database
     *
     * @param book book that user want to update
     * @return the updated book
     */
    @PutMapping("/books")
    private ResponseEntity<Book> updateBook(@RequestBody Book book) {
        return booksService.updateBook(book);
    }

    /**
     * Delete all books from the database
     */
    @DeleteMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    private void deleteAll() {
        booksService.deleteAll();
    }
}
