package kr.mybrary.bookservice.mybook.persistence.repository;

import static kr.mybrary.bookservice.book.persistence.bookInfo.QAuthor.author;
import static kr.mybrary.bookservice.book.persistence.bookInfo.QBookAuthor.bookAuthor;
import static kr.mybrary.bookservice.mybook.persistence.QMyBook.myBook;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.mybook.persistence.MyBookOrderType;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import kr.mybrary.bookservice.mybook.persistence.model.MyBookListDisplayElementModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyBookRepositoryCustomImpl implements MyBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MyBookListDisplayElementModel> findMyBookListDisplayElementModelsByUserId(String userId, MyBookOrderType myBookOrderType, ReadStatus readStatus) {
        List<MyBookListDisplayElementModel> myBookListDisplayElementModel = queryFactory
                .select(Projections.fields(MyBookListDisplayElementModel.class,
                        myBook.id.as("myBookId"),
                        myBook.showable.as("showable"),
                        myBook.exchangeable.as("exchangeable"),
                        myBook.shareable.as("shareable"),
                        myBook.readStatus.as("readStatus"),
                        myBook.startDateOfPossession.as("startDateOfPossession"),
                        myBook.book.id.as("bookId"),
                        myBook.book.title.as("title"),
                        myBook.book.description.as("description"),
                        myBook.book.thumbnailUrl.as("thumbnailUrl"),
                        myBook.book.starRating.as("starRating"),
                        myBook.book.publicationDate.as("publicationDate"))
                )
                .from(myBook)
                .where(myBook.userId.eq(userId),
                        eqReadStatus(readStatus))
                .orderBy(createOrderType(myBookOrderType))
                .fetch();

        for (MyBookListDisplayElementModel model : myBookListDisplayElementModel) {

            List<BookAuthor> fetch = queryFactory
                    .select(bookAuthor)
                    .from(bookAuthor)
                    .where(bookAuthor.book.id.eq(model.getBookId()))
                    .join(bookAuthor.author, author).fetchJoin()
                    .fetch();

            model.setBookAuthors(fetch);
        }

        return myBookListDisplayElementModel;
    }

    @Override
    public Long getBookRegistrationCountOfDay(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59, 999_999_999);

        return queryFactory.select(myBook.count())
                .from(myBook)
                .where(myBook.createdAt.between(start, end))
                .fetchOne();
    }

    private BooleanExpression eqReadStatus(ReadStatus readStatus) {

        if (readStatus == null) {
            return null;
        }
        return myBook.readStatus.eq(readStatus);
    }

    private OrderSpecifier<?> createOrderType(MyBookOrderType myBookOrderType) {

        return Arrays.stream(MyBookOrderType.values())
                .filter(orderType -> orderType == myBookOrderType)
                .findFirst()
                .orElseGet(() -> MyBookOrderType.NONE)
                .getOrderSpecifier();
    }

}
