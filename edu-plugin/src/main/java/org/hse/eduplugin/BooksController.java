package org.hse.eduplugin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private List<Book> getAllBooks() {
        return booksService.getAllBooks();
    }

    /**
     * Get the book with specific id in the database
     *
     * @param id of the book
     * @return the specific book
     */
    @GetMapping("/books/id/{id}")
    private Book getBook(@PathVariable("id") Long id) {
        return booksService.getBookById(id);
    }

    /**
     * Get books with specific title in the database
     *
     * @param title of books
     * @return specific books
     */
    @GetMapping("/books/title/{title}")
    private List<Book> getBooksByTitle(@PathVariable("title") String title) {
        return booksService.getBooksByTitle(title);
    }

    /**
     * Get books with specific author in the database
     *
     * @param author of books
     * @return specific books
     */
    @GetMapping("/books/author/{author}")
    private List<Book> getBooksByAuthor(@PathVariable("author") String author) {
        return booksService.getBooksByAuthor(author);
    }

    /**
     * Get books with specific isbn in the database
     *
     * @param isbn of books
     * @return specific books
     */
    @GetMapping("/books/isbn/{isbn}")
    private List<Book> getBooksByIsbn(@PathVariable("isbn") String isbn) {
        return booksService.getBooksByIsbn(isbn);
    }

    /**
     * Get books with specific printYear in the database
     *
     * @param printYear of books
     * @return specific books
     */
    @GetMapping("/books/printYear/{printYear}")
    private List<Book> getBooksPrintYear(@PathVariable("printYear") Integer printYear) {
        return booksService.getBooksPrintYear(printYear);
    }

    /**
     * Get books with specific readAlready in the database
     *
     * @param readAlready of books
     * @return specific books
     */
    @GetMapping("/books/readAlready/{readAlready}")
    private List<Book> getBooksByReadAlready(@PathVariable("readAlready") Boolean readAlready) {
        return booksService.getBooksByReadAlready(readAlready);
    }

    /**
     * Delete the  book from the database
     *
     * @param id of book that user want to delete
     */
    @DeleteMapping("/books/{id}")
    private void deleteBook(@PathVariable("id") Long id) {
        booksService.deleteBook(id);
    }

    /**
     * Add the new book to the database
     *
     * @param book book that user want to add
     * @return the id of the recently added book
     */
    @PostMapping("/books")
    private Long postBook(@RequestBody Book book) {
        return booksService.postBook(book).getId();
    }

    /**
     * Update the existing book in the database
     *
     * @param book book that user want to update
     * @return the updated book
     */
    @PutMapping("/books")
    private Book updateBook(@RequestBody Book book) {
        return booksService.updateBook(book);
    }
}
