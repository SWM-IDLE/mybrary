package kr.mybrary.bookservice.book.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.presentation.dto.response.BookDetailResponse;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookTranslator;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestElementResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookDtoMapper {

    BookDtoMapper INSTANCE = Mappers.getMapper(BookDtoMapper.class);

    @Mapping(target = "holderCount", constant = "0")
    @Mapping(target = "readCount", constant = "0")
    @Mapping(target = "interestCount", constant = "0")
    @Mapping(target = "starRating", constant = "0")
    @Mapping(target = "reviewCount", constant = "0")
    @Mapping(target = "aladinStarRating", source = "starRating")
    @Mapping(target = "aladinReviewCount", source = "reviewCount")
    Book bookCreateRequestToEntity(BookCreateServiceRequest request);

    @Mapping(target = "authors", source = "bookAuthors", qualifiedByName = "mappingAuthors")
    @Mapping(target = "translators", source = "bookTranslators", qualifiedByName = "mappingTranslators")
    @Mapping(target = "thumbnail", source = "thumbnailUrl")
    @Mapping(target = "category", source = "bookCategory.name")
    @Mapping(target = "categoryId", source = "bookCategory.cid")
    @Mapping(target = "publicationDate", source = "publicationDate", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "starRating", expression = "java(getAverageStarRating(book))")
    BookDetailResponse bookToDetailServiceResponse(Book book);

    @Mapping(target = "aladinStarRating", source = "starRating")
    @Mapping(target = "aladinReviewCount", source = "reviewCount")
    @Mapping(target = "starRating", constant = "0")
    @Mapping(target = "reviewCount", constant = "0")
    BookDetailResponse bookSearchDetailToDetailServiceResponse(BookSearchDetailResponse bookSearchDetailResponse);

    @Mapping(target = "publicationDate", source = "publicationDate", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "thumbnailUrl", source = "thumbnail")
    BookCreateServiceRequest bookSearchDetailToBookCreateServiceRequest(BookSearchDetailResponse bookSearchDetailResponse);

    @Mapping(target = "id", source = "book.id")
    @Mapping(target = "title", source = "book.title")
    @Mapping(target = "isbn13", source = "book.isbn13")
    @Mapping(target = "thumbnailUrl", source = "book.thumbnailUrl")
    @Mapping(target = "author", source = "book.author")
    BookInterestElementResponse bookInterestToBookInterestElementResponse(BookInterest bookInterest);

    @Named("mappingAuthors")
    static List<BookDetailResponse.Author> mappingAuthors(List<BookAuthor> bookAuthors) {
        return bookAuthors.stream()
                .map(BookAuthor::getAuthor)
                .map(author -> BookDetailResponse.Author.builder()
                        .name(author.getName())
                        .authorId(author.getAid())
                        .build()
                ).toList();
    }

    @Named("mappingTranslators")
    static List<BookDetailResponse.Translator> mappingTranslators(List<BookTranslator> bookTranslators) {
        return bookTranslators.stream()
                .map(BookTranslator::getTranslator)
                .map(translator -> BookDetailResponse.Translator.builder()
                        .name(translator.getName())
                        .translatorId(translator.getTid())
                        .build()
                ).toList();
    }

    @Named("stringToLocalDateTime")
    static LocalDateTime stringToLocalDateTime(String publicationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(publicationDate, formatter).atStartOfDay();
    }

    @Named("localDateTimeToString")
    static String localDateTimeToString(LocalDateTime publicationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return publicationDate.format(formatter);
    }

    default double getAverageStarRating(Book book) {
        if (book.getReviewCount() == 0) {
            return 0.0;
        }
        double averageRating = (double) book.getStarRating() / book.getReviewCount();
        return Math.round(averageRating * 10.0) / 10.0;
    }
}