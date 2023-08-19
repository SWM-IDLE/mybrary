package kr.mybrary.bookservice.mybook.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.global.BaseEntity;
import kr.mybrary.bookservice.mybook.domain.dto.request.MybookUpdateServiceRequest;
import kr.mybrary.bookservice.review.persistence.MyReview;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
public class MyBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // TODO: 타입 미정

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @OneToOne(mappedBy = "myBook", fetch = FetchType.LAZY)
    private MyBookMeaningTag myBookMeaningTag;

    @OneToOne(mappedBy = "myBook", fetch = FetchType.LAZY)
    private MyReview myReview;

    private LocalDateTime startDateOfPossession;

    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;

    private boolean deleted;

    public static MyBook of(Book book, String userId) {
        return MyBook.builder()
                .userId(userId)
                .book(book)
                .startDateOfPossession(LocalDateTime.now())
                .readStatus(ReadStatus.TO_READ)
                .showable(true)
                .exchangeable(false)
                .shareable(false)
                .deleted(false)
                .build();
    }

    public boolean isPrivate() {
        return !this.showable;
    }

    public void deleteMyBook() {
        this.deleted = true;
    }

    public void updateFromUpdateRequest(MybookUpdateServiceRequest updateRequest) {

        ReadStatus previousReadStatus = this.readStatus;
        this.readStatus = updateRequest.getReadStatus();
        this.startDateOfPossession = updateRequest.getStartDateOfPossession();
        this.showable = updateRequest.isShowable();
        this.exchangeable = updateRequest.isExchangeable();
        this.shareable = updateRequest.isShareable();

        this.book.adjustReadCount(previousReadStatus, updateRequest.getReadStatus());
    }
}
