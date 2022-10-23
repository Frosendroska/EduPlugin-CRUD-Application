package org.hse.torrent;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByIsbn(String isbn);

    List<Book> findByPrintYear(Integer printYear);

    List<Book> findByReadAlready(Boolean readAlready);
}
