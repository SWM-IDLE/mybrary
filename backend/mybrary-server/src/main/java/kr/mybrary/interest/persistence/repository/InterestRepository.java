package kr.mybrary.interest.persistence.repository;

import kr.mybrary.interest.persistence.Interest;
import kr.mybrary.interest.persistence.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findByName(String name);
    List<Interest> findAllByCategory(InterestCategory category);

}
