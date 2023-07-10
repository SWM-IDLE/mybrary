package kr.mybrary.bookservice.mybook.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.global.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_hold_books")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // TODO: 타입 미정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    private LocalDateTime startDateOfPossession;

    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;

    private boolean deleted;

    public static MyBook of(Book book, String userId) {
        return MyBook.builder()
                .userId(userId)
                .book(book)
                .startDateOfPossession(null)
                .readStatus(ReadStatus.TO_READ)
                .showable(true)
                .exchangeable(false)
                .shareable(false)
                .deleted(false)
                .build();
    }
}
