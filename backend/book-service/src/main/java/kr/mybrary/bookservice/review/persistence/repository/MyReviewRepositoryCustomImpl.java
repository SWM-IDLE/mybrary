package kr.mybrary.bookservice.review.persistence.repository;

import static kr.mybrary.bookservice.review.persistence.QMyReview.myReview;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.review.persistence.model.MyReviewElementModel;
import kr.mybrary.bookservice.review.persistence.model.MyReviewFromMyBookModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyReviewRepositoryCustomImpl implements MyReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MyReviewElementModel> findReviewsByBook(Book book) {
        return queryFactory
                .select(Projections.fields(MyReviewElementModel.class,
                        myReview.id.as("id"),
                        myReview.myBook.userId.as("userId"),
                        myReview.content.as("content"),
                        myReview.starRating.as("starRating"),
                        myReview.createdAt.as("createdAt")))
                .from(myReview)
                .where(myReview.myBook.book.eq(book))
                .fetch();
    }

    @Override
    public Optional<MyReviewFromMyBookModel> findReviewByMyBook(MyBook myBook) {

        MyReviewFromMyBookModel myReviewFromMyBookModel = queryFactory
                .select(Projections.fields(MyReviewFromMyBookModel.class,
                        myReview.id.as("id"),
                        myReview.content.as("content"),
                        myReview.starRating.as("starRating"),
                        myReview.createdAt.as("createdAt"),
                        myReview.updatedAt.as("updatedAt")))
                .from(myReview)
                .where(myReview.myBook.eq(myBook))
                .fetchOne();

        return Optional.ofNullable(myReviewFromMyBookModel);
    }
}
