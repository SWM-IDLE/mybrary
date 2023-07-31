package kr.mybrary.userservice.interest.persistence.repository;

import kr.mybrary.userservice.interest.InterestFixture;
import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import kr.mybrary.userservice.user.UserFixture;
import kr.mybrary.userservice.user.persistence.User;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
class UserInterestRepositoryTest {

    @Autowired
    private UserInterestRepository userInterestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("페치 조인을 사용하여 해당 사용자의 관심사를 모두 가져온다.")
    void findAllByUserWithInterestUsingFetchJoin() {
        // given
        User savedUser = userRepository.save(UserFixture.COMMON_USER.getUser());
        Interest savedInterest1 =  interestRepository.save(InterestFixture.DOMESTIC_NOVEL.getInterest());
        Interest savedInterest2 =  interestRepository.save(InterestFixture.FOREIGN_NOVEL.getInterest());

        userInterestRepository.save(UserInterest.builder()
                .user(savedUser)
                .interest(savedInterest1)
                .build());
        userInterestRepository.save(UserInterest.builder()
                .user(savedUser)
                .interest(savedInterest2)
                .build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<UserInterest> foundUserInterests = userInterestRepository.findAllByUserWithInterestUsingFetchJoin(savedUser);

        // then
        assertAll(
                () -> assertThat(foundUserInterests).isNotNull(),
                () -> assertThat(foundUserInterests.size()).isEqualTo(2),
                () -> assertThat(foundUserInterests.get(0).getUser().getId()).isEqualTo(savedUser.getId()),
                () -> assertThat(foundUserInterests.get(1).getUser().getId()).isEqualTo(savedUser.getId()),
                () -> assertThat(foundUserInterests.get(0).getInterest().getName()).isEqualTo("국내소설"),
                () -> assertThat(foundUserInterests.get(1).getInterest().getName()).isEqualTo("외국소설"),
                () -> assertThat(foundUserInterests.get(0).getInterest()).isNotInstanceOf(HibernateProxy.class),
                () -> assertThat(foundUserInterests.get(1).getInterest()).isNotInstanceOf(HibernateProxy.class)
        );
    }
}