package kr.mybrary.bookservice.mybook.persistence.repository;

import kr.mybrary.bookservice.mybook.persistence.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {

    boolean existsByUserIdAndBookId(String userId, Long id);
}
