package org.hse.torrent;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BooksService {
    @Autowired
    BooksRepository booksRepository;

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        booksRepository.findAll().forEach(books::add);
        return books;
    }

    public Book getBookById(Long id) {
        var book = booksRepository.findById(id);
        return book.orElse(null);
    }

    public Book postBook(Book book) {
        return booksRepository.save(book);
    }

    public Book updateBook(Book book) {
        if (getBookById(book.getId()) != null) {
            return booksRepository.save(book);
        }
        return null;
    }

    public void deleteBook(Long id) {
        booksRepository.deleteById(id);
    }

    public List<Book> getBooksByTitle(String title) {
        return booksRepository.findByTitle(title);
    }

    public List<Book> getBooksByAuthor(String author) {
        return booksRepository.findByAuthor(author);
    }

    public List<Book> getBooksByIsbn(String isbn) {
        return booksRepository.findByIsbn(isbn);
    }

    public List<Book> getBooksPrintYear(Integer printYear) {
        return booksRepository.findByPrintYear(printYear);
    }

    public List<Book> getBooksByReadAlready(Boolean readAlready) {
        return booksRepository.findByReadAlready(readAlready);
    }
}
