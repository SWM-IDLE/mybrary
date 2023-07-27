package kr.mybrary.userservice.user.persistence.repository;

import kr.mybrary.userservice.user.persistence.Interest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
class InterestRepositoryTest {

    @Autowired
    private InterestRepository interestRepository;

    @Test
    @DisplayName("관심사명으로 관심사를 가져온다.")
    void findByName() {
        // given
        Interest savedInterest = interestRepository.save(Interest.builder()
            .name("테스트 관심사")
            .build());

        // when
        Interest foundInterest = interestRepository.findByName(savedInterest.getName()).get();

        // then
        assertAll(
            () -> assertNotNull(foundInterest),
            () -> assertEquals(savedInterest.getId(), foundInterest.getId()),
            () -> assertEquals(savedInterest.getName(), foundInterest.getName())
        );
    }

}