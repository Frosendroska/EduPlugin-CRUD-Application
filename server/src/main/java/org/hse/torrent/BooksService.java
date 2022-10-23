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

    public void postOrUpdate(Book book) {
        booksRepository.save(book);
    }

    public void deleteBook(Long id) {
        booksRepository.deleteById(id);
    }
}
