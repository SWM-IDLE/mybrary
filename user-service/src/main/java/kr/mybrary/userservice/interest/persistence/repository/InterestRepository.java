package kr.mybrary.userservice.interest.persistence.repository;

import kr.mybrary.userservice.interest.persistence.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findAll();

    Optional<Interest> findByName(String name);

}
