package kr.mybrary.bookservice.book.persistence;

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
import kr.mybrary.bookservice.global.BaseEntity;
import lombok.Getter;

@Entity
@Table(name = "users_hold_books")
@Getter
public class MyBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // TODO: 타입 미정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book bookId;

    private boolean isPublic;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    private LocalDateTime startDateOfPossession;
    private boolean isExchangeable;
    private boolean isShareable;

    private boolean isDeleted;
}
