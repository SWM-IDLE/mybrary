package kr.mybrary.global.config;

import kr.mybrary.book.persistence.Book;
import kr.mybrary.book.persistence.bookInfo.Author;
import kr.mybrary.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.book.persistence.bookInfo.BookCategory;
import kr.mybrary.book.persistence.repository.BookCategoryRepository;
import kr.mybrary.book.persistence.repository.BookRepository;
import kr.mybrary.mybook.persistence.MyBook;
import kr.mybrary.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.user.persistence.Role;
import kr.mybrary.user.persistence.User;
import kr.mybrary.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class LocalDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final MyBookRepository myBookRepository;

    private static final String ADMIN_LOGIN_ID = "admin";
    private static final String ADMIN_NICKNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Override
    @Transactional
    public void run(String... args) {
        User admin = initAdmin();
        initBooks(admin);
    }

    private User initAdmin() {
        if (userRepository.existsByLoginId(ADMIN_LOGIN_ID)) {
            return userRepository.findByLoginId(ADMIN_LOGIN_ID).orElseThrow();
        }
        User admin = User.builder()
                .loginId(ADMIN_LOGIN_ID)
                .nickname(ADMIN_NICKNAME)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role(Role.ADMIN)
                .introduction("로컬 개발용 어드민 계정")
                .profileImageUrl("")
                .profileImageThumbnailTinyUrl("")
                .profileImageThumbnailSmallUrl("")
                .build();
        userRepository.save(admin);
        log.info("===== [LOCAL] 어드민 계정 생성: {} / {} =====", ADMIN_LOGIN_ID, ADMIN_PASSWORD);
        return admin;
    }

    private void initBooks(User admin) {
        // 도서가 이미 있어도 admin MyBook이 없으면 등록
        if (bookRepository.count() > 0) {
            if (myBookRepository.countByUserId(ADMIN_LOGIN_ID) == 0) {
                bookRepository.findAll().forEach(book ->
                        myBookRepository.save(MyBook.of(book, ADMIN_LOGIN_ID)));
                log.info("===== [LOCAL] admin MyBook {}권 재등록 완료 =====", bookRepository.count());
            }
            return;
        }

        BookCategory novel = bookCategoryRepository.save(BookCategory.builder().cid(1).name("소설").build());
        BookCategory selfHelp = bookCategoryRepository.save(BookCategory.builder().cid(2).name("자기계발").build());
        BookCategory science = bookCategoryRepository.save(BookCategory.builder().cid(3).name("과학").build());
        BookCategory history = bookCategoryRepository.save(BookCategory.builder().cid(4).name("역사").build());
        BookCategory it = bookCategoryRepository.save(BookCategory.builder().cid(5).name("IT/컴퓨터").build());

        List<BookSeed> seeds = List.of(
            new BookSeed("9788936434588", "채식주의자", "한강", "문학동네",
                "https://image.aladin.co.kr/product/20/11/cover/8936434586_1.jpg",
                "한강의 부커상 수상작. 평범한 여자가 어느 날 채식주의자가 되기로 결심하며 시작되는 이야기.", novel, 247, 13800),
            new BookSeed("9788936433598", "소년이 온다", "한강", "창비",
                "https://image.aladin.co.kr/product/7897/1/cover/8936433598_1.jpg",
                "5·18 광주민주화운동을 배경으로 한 한강의 장편소설.", novel, 216, 14000),
            new BookSeed("9788901259673", "아몬드", "손원평", "창비",
                "https://image.aladin.co.kr/product/19504/13/cover/8901259672_1.jpg",
                "감정을 느끼지 못하는 소년 윤재의 성장 이야기.", novel, 264, 13500),
            new BookSeed("9788960776708", "미움받을 용기", "기시미 이치로", "인플루엔셜",
                "https://image.aladin.co.kr/product/5466/13/cover/896077670X_2.jpg",
                "아들러 심리학을 바탕으로 자유롭고 행복한 삶을 위한 철학적 대화.", selfHelp, 336, 15800),
            new BookSeed("9788937460449", "코스모스", "칼 세이건", "사이언스북스",
                "https://image.aladin.co.kr/product/163/43/cover/8937460440_1.jpg",
                "칼 세이건의 우주 과학 명저. 인류와 우주의 관계를 탐구한다.", science, 758, 28000),
            new BookSeed("9788934990772", "사피엔스", "유발 하라리", "김영사",
                "https://image.aladin.co.kr/product/6247/90/cover/8934990775_2.jpg",
                "인류의 역사를 거시적 관점으로 바라본 베스트셀러.", history, 636, 22000),
            new BookSeed("9791162241691", "클린 코드", "로버트 C. 마틴", "인사이트",
                "https://image.aladin.co.kr/product/1449/42/cover/8966260950_1.jpg",
                "읽기 좋은 코드를 작성하는 방법을 설명하는 소프트웨어 개발 명저.", it, 584, 33000),
            new BookSeed("9791190665162", "82년생 김지영", "조남주", "민음사",
                "https://image.aladin.co.kr/product/16235/50/cover/8937488604_1.jpg",
                "대한민국 평범한 여성의 삶을 통해 성차별 문제를 조명한 소설.", novel, 190, 13000),
            new BookSeed("9788954631150", "원씽", "게리 켈러", "비즈니스북스",
                "https://image.aladin.co.kr/product/5527/38/cover/895463115X_1.jpg",
                "한 가지에 집중하는 삶의 방식을 제안하는 자기계발서.", selfHelp, 292, 16000),
            new BookSeed("9791186900956", "파친코", "이민진", "인플루엔셜",
                "https://image.aladin.co.kr/product/23558/27/cover/K762637011_1.jpg",
                "재일교포 4대에 걸친 삶을 그린 대하 역사소설.", history, 864, 25000)
        );

        for (BookSeed s : seeds) {
            Author author = Author.builder().aid(0).name(s.authorName).build();
            Book book = Book.builder()
                    .isbn13(s.isbn13)
                    .title(s.title)
                    .author(s.authorName)
                    .thumbnailUrl(s.thumbnailUrl)
                    .publisher(s.publisher)
                    .description(s.description)
                    .pages(s.pages)
                    .priceStandard(s.price)
                    .priceSales(s.price)
                    .publicationDate(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .holderCount(0)
                    .readCount(0)
                    .interestCount(0)
                    .starRating(0.0)
                    .reviewCount(0)
                    .aladinStarRating(0.0)
                    .aladinReviewCount(0)
                    .bookCategory(s.category)
                    .build();

            BookAuthor bookAuthor = BookAuthor.builder().author(author).build();
            book.addBookAuthor(List.of(bookAuthor));
            Book savedBook = bookRepository.save(book);

            // 어드민 MyBook 등록
            myBookRepository.save(MyBook.of(savedBook, ADMIN_LOGIN_ID));
        }

        log.info("===== [LOCAL] 샘플 도서 {}권 + MyBook 등록 완료 =====", seeds.size());
    }

    private record BookSeed(String isbn13, String title, String authorName, String publisher,
                            String thumbnailUrl, String description, BookCategory category,
                            int pages, int price) {}
}
