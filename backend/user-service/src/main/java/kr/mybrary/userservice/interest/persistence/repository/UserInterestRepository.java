package kr.mybrary.userservice.interest.persistence.repository;

import kr.mybrary.userservice.interest.persistence.UserInterest;
import kr.mybrary.userservice.user.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    @Query("SELECT distinct ui FROM UserInterest ui join fetch ui.interest")
    List<UserInterest> findAllByUserWithInterestUsingFetchJoin(User user);

}
