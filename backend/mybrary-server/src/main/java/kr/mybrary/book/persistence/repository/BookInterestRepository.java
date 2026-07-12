package kr.mybrary.book.persistence.repository;

import java.util.Optional;
import kr.mybrary.book.persistence.Book;
import kr.mybrary.book.persistence.BookInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInterestRepository extends JpaRepository<BookInterest, Long>, BookInterestRepositoryCustom {

    Optional<BookInterest> findByBookAndUserId(Book book, String userId);

    boolean existsByBookAndUserId(Book book, String userId);
}
