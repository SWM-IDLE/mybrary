package kr.mybrary.book.persistence.repository;

import java.util.Optional;
import kr.mybrary.book.persistence.bookInfo.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    Optional<BookCategory> findByCid(Integer cid);
}
