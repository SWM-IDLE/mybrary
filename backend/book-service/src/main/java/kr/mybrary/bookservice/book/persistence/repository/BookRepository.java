package kr.mybrary.bookservice.book.persistence.repository;

import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn10OrIsbn13(String isbn10, String isbn13);
}
