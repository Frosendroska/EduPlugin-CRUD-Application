package org.hse.eduplugin;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BooksService {
    @Autowired
    BooksRepository booksRepository;

    public void deleteAll() {
        booksRepository.deleteAll();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        booksRepository.findAll().forEach(books::add);
        return books;
    }

    public Book getBookById(Long id) {
        var book = booksRepository.findById(id);
        return book.orElse(null);
    }

    public Long postBook(Book book) {
        return booksRepository.save(book).getId();
    }

    public void postAllBook(List<Book> books) {
        booksRepository.saveAll(books);
    }

    public Book updateBook(Book book) {
        if (getBookById(book.getId()) != null) {
            return booksRepository.save(book);
        }
        return null;
    }

    public Book deleteBook(Long id) {
        Book book = getBookById(id);
        if (book != null) {
            booksRepository.deleteById(id);
        }
        return book;
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
