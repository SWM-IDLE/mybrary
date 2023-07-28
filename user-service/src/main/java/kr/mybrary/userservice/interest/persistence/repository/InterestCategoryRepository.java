package kr.mybrary.userservice.interest.persistence.repository;

import kr.mybrary.userservice.interest.persistence.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {
    @Query("SELECT distinct ic FROM InterestCategory ic join fetch ic.interests")
    List<InterestCategory> findAllWithInterestUsingFetchJoin();

}
