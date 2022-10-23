package org.hse.torrent;

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
     * Get book with specific id in the database
     *
     * @param id of the book
     * @return the specific book
     */
    @GetMapping("/books/{id}")
    private Book getBook(@PathVariable("id") Long id) {
        return booksService.getBookById(id);
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
        booksService.postOrUpdate(book);
        return book.getId();
    }

    /**
     * Update the existing book in the database
     *
     * @param book book that user want to update
     * @return the updated book
     */
    @PutMapping("/books")
    private Book updateBook(@RequestBody Book book) {
        booksService.postOrUpdate(book);
        return book;
    }
}
