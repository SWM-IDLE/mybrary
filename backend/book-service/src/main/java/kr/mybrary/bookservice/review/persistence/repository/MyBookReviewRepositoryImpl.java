package kr.mybrary.bookservice.review.persistence.repository;

import static kr.mybrary.bookservice.review.persistence.QMyBookReview.myBookReview;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.review.persistence.dto.MyBookReviewElementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyBookReviewRepositoryImpl implements MyBookReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MyBookReviewElementDto> findReviewsByBook(Book book) {
        return queryFactory
                .select(Projections.fields(MyBookReviewElementDto.class,
                        myBookReview.myBook.userId.as("userId"),
                        myBookReview.content.as("content"),
                        myBookReview.starRating.as("starRating"),
                        myBookReview.createdAt.as("createdAt")))
                .from(myBookReview)
                .fetch();
    }
}
