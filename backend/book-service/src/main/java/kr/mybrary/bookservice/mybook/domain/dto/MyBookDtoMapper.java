package kr.mybrary.bookservice.mybook.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookTranslator;
import kr.mybrary.bookservice.global.util.DateUtils;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyBookDtoMapper {

    MyBookDtoMapper INSTANCE = Mappers.getMapper(MyBookDtoMapper.class);

    @Mapping(target = "book.stars", constant = "0.0")
    MyBookElementResponse entityToMyBookElementResponse(MyBook myBook);

    @Mapping(target = "book.stars", constant = "0.0")
    @Mapping(target = "book.authors", source = "book.bookAuthors", qualifiedByName = "getAuthors")
    @Mapping(target = "book.translators", source = "book.bookTranslators", qualifiedByName = "getTranslators")
    @Mapping(target = "meaningTag.quote", expression = "java(myBook.getMyBookMeaningTag() != null ? myBook.getMyBookMeaningTag().getMeaningTag().getQuote() : \"\")")
    @Mapping(target = "meaningTag.colorCode", expression = "java(myBook.getMyBookMeaningTag() != null ? myBook.getMyBookMeaningTag().getMeaningTagColor() : \"\")")
    @Mapping(target = "review.id", source = "myBookReview.id")
    @Mapping(target = "review.content", source = "myBookReview.content")
    @Mapping(target = "review.starRating", source = "myBookReview.starRating")
    @Mapping(target = "review.createdAt", source = "myBookReview.createdAt", qualifiedByName = "toFormatYYYMMddHHmm")
    MyBookDetailResponse entityToMyBookDetailResponse(MyBook myBook);

    @Named("getAuthors")
    static List<String> getAuthors(List<BookAuthor> bookAuthors) {
        return bookAuthors.stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getName())
                .toList();
    }

    @Named("getTranslators")
    static List<String> getTranslators(List<BookTranslator> bookTranslators) {
        return bookTranslators.stream()
                .map(bookTranslator -> bookTranslator.getTranslator().getName())
                .toList();
    }

    @Named("toFormatYYYMMddHHmm")
    static String toFormatYYYMMddHHmm(LocalDateTime dateTime) {
        return DateUtils.toFormatYYYMMddHHmm(dateTime);
    }
}
