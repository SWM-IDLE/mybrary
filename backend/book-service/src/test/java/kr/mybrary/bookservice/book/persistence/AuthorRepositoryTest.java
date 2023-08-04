package kr.mybrary.bookservice.book.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.book.persistence.bookInfo.Author;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
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

    @DisplayName("저자의 식별 ID로 조회한다.")
    @Test
    void findAuthorByAid() {

        // given
        Author savedAuthor = authorRepository.save(createAuthor());

        entityManager.flush();
        entityManager.clear();

        // when
        Author foundAuthor = authorRepository.findByAid(savedAuthor.getAid()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(foundAuthor.getAid()).isEqualTo(savedAuthor.getAid()),
                () -> assertThat(foundAuthor.getName()).isEqualTo(savedAuthor.getName())
        );
    }

    private Author createAuthor() {
        return Author.builder()
                .name("author_name")
                .aid(1122)
                .build();
    }
}