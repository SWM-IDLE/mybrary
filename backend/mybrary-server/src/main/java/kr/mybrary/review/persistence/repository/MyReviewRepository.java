package kr.mybrary.review.persistence.repository;

import java.util.Optional;
import kr.mybrary.mybook.persistence.MyBook;
import kr.mybrary.review.persistence.MyReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyReviewRepository extends JpaRepository<MyReview, Long>, MyReviewRepositoryCustom {

    boolean existsByMyBook(MyBook myBook);

    @Query("select mr from MyReview mr "
            + "join fetch mr.myBook mb "
            + "where mr.id = :id")
    Optional<MyReview> findByIdWithMyBookUsingFetchJoin(Long id);
}
