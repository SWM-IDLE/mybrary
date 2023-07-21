package kr.mybrary.bookservice.tag.persistence.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.MyBookMeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyBookMeaningTagRepositoryTest {

    @Autowired
    private MyBookMeaningTagRepository myBookMeaningTagRepository;

    @Autowired
    private MyBookRepository myBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MeaningTagRepository meaningTagRepository;

    @DisplayName("마이북을 통해 마이북_의미태그 매핑 테이블을 조회한다.")
    @Test
    void findByMyBook() {
        // given
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK.getBook());
        MyBook savedMyBook = myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook));
        MeaningTag savedMeaningTag = meaningTagRepository.save(
                MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag());

        myBookMeaningTagRepository.save(MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTag(savedMyBook, savedMeaningTag));

        // when
        Optional<MyBookMeaningTag> findMyBookMeaningTag = myBookMeaningTagRepository.findByMyBook(savedMyBook);

        // when
        assertAll(
                () -> {
                    assertThat(findMyBookMeaningTag.isPresent()).isTrue();
                    assertThat(findMyBookMeaningTag.get().getMyBook()).isEqualTo(savedMyBook);
                    assertThat(findMyBookMeaningTag.get().getMeaningTag()).isEqualTo(savedMeaningTag);
                }
        );
    }
}