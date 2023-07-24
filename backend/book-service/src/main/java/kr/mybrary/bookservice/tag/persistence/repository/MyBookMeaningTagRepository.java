package kr.mybrary.bookservice.tag.persistence.repository;

import java.util.Optional;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookMeaningTagRepository extends JpaRepository<MyBookMeaningTag, Long> {

    Optional<MyBookMeaningTag> findByMyBook(MyBook myBook);

    void deleteByMyBook(MyBook myBook);
}
