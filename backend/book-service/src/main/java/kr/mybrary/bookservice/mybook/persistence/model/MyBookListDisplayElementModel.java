package kr.mybrary.bookservice.mybook.persistence.model;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyBookListDisplayElementModel {

    private Long myBookId;
    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;
    private ReadStatus readStatus;
    private LocalDateTime startDateOfPossession;

    private Long bookId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Double starRating;
    private LocalDateTime publicationDate;
    private List<BookAuthor> bookAuthors;

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }
}
