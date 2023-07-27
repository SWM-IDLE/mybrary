package kr.mybrary.userservice.interest.persistence.repository;

import kr.mybrary.userservice.interest.persistence.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {
}
