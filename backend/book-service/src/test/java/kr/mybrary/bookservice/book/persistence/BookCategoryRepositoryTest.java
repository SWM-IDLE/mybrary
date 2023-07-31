package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.book.persistence.repository.BookCategoryRepository;
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
class BookCategoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookCategoryRepository bookCategoryRepository;

    @DisplayName("카테고리의 식별 ID로 조회한다.")
    @Test
    void findCategoryByCid() {

        // given
        BookCategory savedBookCategory = bookCategoryRepository.save(createBookCategory());

        entityManager.flush();
        entityManager.clear();

        // when
        BookCategory findBookCategory = bookCategoryRepository.findByCid(savedBookCategory.getCid()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findBookCategory.getCid()).isEqualTo(savedBookCategory.getCid()),
                () -> assertThat(findBookCategory.getName()).isEqualTo(savedBookCategory.getName())
        );
    }

    private BookCategory createBookCategory() {
        return BookCategory.builder()
                .name("category_name")
                .cid(1122)
                .build();
    }
}