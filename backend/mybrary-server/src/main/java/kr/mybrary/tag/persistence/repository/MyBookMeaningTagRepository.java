package kr.mybrary.tag.persistence.repository;

import java.util.Optional;
import kr.mybrary.mybook.persistence.MyBook;
import kr.mybrary.tag.persistence.MyBookMeaningTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookMeaningTagRepository extends JpaRepository<MyBookMeaningTag, Long> {

    Optional<MyBookMeaningTag> findByMyBook(MyBook myBook);

    void deleteByMyBook(MyBook myBook);
}
