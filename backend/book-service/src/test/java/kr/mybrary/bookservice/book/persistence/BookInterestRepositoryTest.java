package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.BookInterestFixture;
import kr.mybrary.bookservice.book.persistence.repository.BookInterestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookInterestRepositoryTest {

    @Autowired
    BookInterestRepository bookInterestRepository;

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("도서와 사용자 아이디로 관심 도서를 조회한다.")
    @Test
    void findByBookAndUserId() {

        // given
        Book book = entityManager.persist(BookFixture.COMMON_BOOK.getBookBuilder().id(null).build());

        BookInterest bookInterest = BookInterestFixture.COMMON_BOOK_INTEREST.getBookInterestBuilder()
                .id(null)
                .book(book)
                .build();

        entityManager.persist(bookInterest);

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<BookInterest> foundBookInterest = bookInterestRepository.findByBookAndUserId(bookInterest.getBook(),
                bookInterest.getUserId());

        // then
        assertAll(
                () -> {
                    assertThat(foundBookInterest.isPresent()).isTrue();
                    assertThat(foundBookInterest.get().getBook().getIsbn13()).isEqualTo(book.getIsbn13());
                    assertThat(foundBookInterest.get().getUserId()).isEqualTo(bookInterest.getUserId());
                }
        );
    }
}