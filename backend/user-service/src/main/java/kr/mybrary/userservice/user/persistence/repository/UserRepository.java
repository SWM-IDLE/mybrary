package kr.mybrary.userservice.user.persistence.repository;

import kr.mybrary.userservice.user.persistence.SocialType;
import kr.mybrary.userservice.user.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    List<User> findByNicknameContaining(String nickname);
}
