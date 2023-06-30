package kr.mybrary.bookservice.mybook.persistence.repository;

import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {

    boolean existsByUserIdAndBook(String userId, Book book);
}
