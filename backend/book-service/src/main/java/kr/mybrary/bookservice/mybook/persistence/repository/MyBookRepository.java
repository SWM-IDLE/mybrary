package kr.mybrary.bookservice.mybook.persistence.repository;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyBookRepository extends JpaRepository<MyBook, Long>, MyBookRepositoryCustom {

    boolean existsByUserIdAndBook(String userId, Book book);

    List<MyBook> findAllByUserId(String userId);

    @Query("select m from MyBook m "
            + "left join fetch m.book "
            + "left join fetch m.myBookMeaningTag mbt "
            + "left join fetch mbt.meaningTag mt "
            + "where mt.quote = :quote")
    List<MyBook> findByMeaningTagQuote(String quote);

    @Query("select m from MyBook m "
            + "left join fetch m.book "
            + "left join fetch m.myBookMeaningTag mbt "
            + "left join fetch mbt.meaningTag "
            + "where m.id = :mybookId")
    Optional<MyBook> findMyBookDetailUsingFetchJoin(Long mybookId);

    @Query("select m from MyBook m "
            + "left join fetch m.book "
            + "where m.id = :mybookId")
    Optional<MyBook> findByIdWithBook(Long mybookId);

    Optional<MyBook> findByUserIdAndBook(String userId, Book book);
}
