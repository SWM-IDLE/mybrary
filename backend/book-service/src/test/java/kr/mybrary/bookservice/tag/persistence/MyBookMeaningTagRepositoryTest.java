package kr.mybrary.bookservice.tag.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.PersistenceTest;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.MyBookMeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.repository.MyBookMeaningTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@PersistenceTest
class MyBookMeaningTagRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

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
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook savedMyBook = myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook));
        MeaningTag savedMeaningTag = meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag());

        myBookMeaningTagRepository.save(
                MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTag(savedMyBook, savedMeaningTag));

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<MyBookMeaningTag> foundMyBookMeaningTag = myBookMeaningTagRepository.findByMyBook(savedMyBook);

        // when
        assertAll(
                () -> {
                    assertThat(foundMyBookMeaningTag.isPresent()).isTrue();
                    assertThat(foundMyBookMeaningTag.get().getMyBook().getBook().getIsbn10()).isEqualTo(
                            savedMyBook.getBook().getIsbn10());
                    assertThat(foundMyBookMeaningTag.get().getMeaningTag().getQuote()).isEqualTo(
                            savedMeaningTag.getQuote());
                }
        );
    }

    @DisplayName("마이북을 통해 마이북_의미태그 매핑 테이블을 삭제한다.")
    @Test
    void deleteByMyBook() {

        // given
        Book savedBook = bookRepository.save(BookFixture.COMMON_BOOK_WITHOUT_RELATION.getBook());
        MyBook savedMyBook = myBookRepository.save(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookWithBook(savedBook));
        MeaningTag savedMeaningTag = meaningTagRepository.save(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag());

        myBookMeaningTagRepository.save(
                MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTag(savedMyBook, savedMeaningTag));

        entityManager.flush();
        entityManager.clear();

        // when
        MyBook foundMyBook = myBookRepository.findById(savedMyBook.getId()).orElseThrow();

        myBookMeaningTagRepository.deleteByMyBook(foundMyBook);

        // then
        Optional<MyBookMeaningTag> findMyBookMeaningTag = myBookMeaningTagRepository.findByMyBook(foundMyBook);
        List<MyBookMeaningTag> myBookMeaningTags = myBookMeaningTagRepository.findAll();

        assertAll(
                () -> assertThat(findMyBookMeaningTag.isPresent()).isFalse(),
                () -> assertThat(myBookMeaningTags.size()).isEqualTo(0)
        );
    }
}