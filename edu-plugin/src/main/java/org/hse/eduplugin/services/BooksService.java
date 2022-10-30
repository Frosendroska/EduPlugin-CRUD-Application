package org.hse.eduplugin.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.hse.eduplugin.repositories.BooksRepository;
import org.hse.eduplugin.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BooksService {
    @Autowired
    BooksRepository booksRepository;

    public void deleteAll() {
        booksRepository.deleteAll();
    }

    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = new ArrayList<>();
        booksRepository.findAll().forEach(books::add);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<Book> getBookById(Long id) {
        var book = booksRepository.findById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Long> postBook(Book book) {
        return new ResponseEntity<>(booksRepository.save(book).getId(), HttpStatus.CREATED);
    }

    public void postAllBook(List<Book> books) {
        booksRepository.saveAll(books);
    }

    public ResponseEntity<Book> updateBook(Book book) {
        if (booksRepository.findById(book.getId()).isPresent()) {
            return new ResponseEntity<>(booksRepository.save(book), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Book> deleteBook(Long id) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()) {
            booksRepository.deleteById(id);
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Book>> getBooksByTitle(String title) {
        var books = booksRepository.findByTitle(title);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<List<Book>> getBooksByAuthor(String author) {
        var books = booksRepository.findByAuthor(author);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<List<Book>> getBooksByIsbn(String isbn) {
        var books = booksRepository.findByIsbn(isbn);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<List<Book>> getBooksPrintYear(Integer printYear) {
        var books = booksRepository.findByPrintYear(printYear);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<List<Book>> getBooksByReadAlready(Boolean readAlready) {
        var books = booksRepository.findByReadAlready(readAlready);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
