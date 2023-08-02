package kr.mybrary.bookservice.book.persistence.repository;

import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn10OrIsbn13(String isbn10, String isbn13);

    Optional<Book> findByIsbn13(String isbn13);

    @Query("select b from Book b "
            + "join fetch b.bookAuthors ba "
            + "join fetch ba.author baa "
            + "join fetch b.bookCategory bc "
            + "where b.isbn13 = :isbn13 or b.isbn10 = :isbn10")
    Optional<Book> findByISBNWithAuthorAndCategoryUsingFetchJoin(String isbn10, String isbn13);
}
