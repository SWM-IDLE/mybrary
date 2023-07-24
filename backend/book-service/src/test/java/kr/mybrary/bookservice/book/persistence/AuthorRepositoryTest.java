package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.*;

import kr.mybrary.bookservice.book.persistence.author.Author;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AuthorRepository authorRepository;

    @DisplayName("저자를 저장한다.")
    @Test
    void saveAuthor() {

        // given
        Author author = createAuthor();

        // when
        Author savedAuthor = authorRepository.save(author);

        // then
        assertThat(savedAuthor.getName()).isEqualTo(author.getName());
    }

    @DisplayName("저자를 이름으로 조회한다.")
    @Test
    void findAuthorByName() {

        // given
        Author savedAuthor = authorRepository.save(createAuthor());

        entityManager.flush();
        entityManager.clear();

        // when
        Author foundAuthor = authorRepository.findByName(savedAuthor.getName()).orElseThrow();

        // then
        assertThat(foundAuthor.getName()).isEqualTo(savedAuthor.getName());
    }

    private Author createAuthor() {
        return Author.builder()
                .name("author_name")
                .build();
    }
}