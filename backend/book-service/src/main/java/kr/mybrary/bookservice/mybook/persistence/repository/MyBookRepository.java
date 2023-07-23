package kr.mybrary.bookservice.mybook.persistence.repository;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {

    boolean existsByUserIdAndBook(String userId, Book book);

    List<MyBook> findAllByUserId(String userId);

    Optional<MyBook> findByIdAndDeletedIsFalse(Long bookId);

    @Query("select m from MyBook m join fetch m.myBookMeaningTag mbt "
            + "join fetch mbt.meaningTag "
            + "mt where mt.quote = :quote")
    List<MyBook> findByMeaningTagQuote(String quote);
}
