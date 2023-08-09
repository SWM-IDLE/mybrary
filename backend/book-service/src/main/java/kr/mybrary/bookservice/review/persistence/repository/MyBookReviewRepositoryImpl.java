package kr.mybrary.bookservice.review.persistence.repository;

import static kr.mybrary.bookservice.review.persistence.QMyBookReview.myBookReview;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.model.MyBookReviewElementDto;
import kr.mybrary.bookservice.review.persistence.model.ReviewFromMyBookModel;
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
                        myBookReview.id.as("id"),
                        myBookReview.myBook.userId.as("userId"),
                        myBookReview.content.as("content"),
                        myBookReview.starRating.as("starRating"),
                        myBookReview.createdAt.as("createdAt")))
                .from(myBookReview)
                .fetch();
    }

    @Override
    public Optional<ReviewFromMyBookModel> findReviewByMyBook(MyBook myBook) {

        ReviewFromMyBookModel reviewFromMyBookModel = queryFactory
                .select(Projections.fields(ReviewFromMyBookModel.class,
                        myBookReview.id.as("id"),
                        myBookReview.content.as("content"),
                        myBookReview.starRating.as("starRating"),
                        myBookReview.createdAt.as("createdAt"),
                        myBookReview.updatedAt.as("updatedAt")))
                .from(myBookReview)
                .where(myBookReview.myBook.eq(myBook))
                .fetchOne();

        return Optional.ofNullable(reviewFromMyBookModel);
    }
}
