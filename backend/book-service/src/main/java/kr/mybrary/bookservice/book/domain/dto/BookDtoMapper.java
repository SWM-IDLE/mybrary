package kr.mybrary.bookservice.book.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.response.BookDetailServiceResponse;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookTranslator;
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


    @Mapping(target = "starRating", source = "aladinStarRating")
    @Mapping(target = "reviewCount", source = "aladinReviewCount")
    @Mapping(target = "authors", source = "bookAuthors", qualifiedByName = "mappingAuthors")
    @Mapping(target = "translators", source = "bookTranslators", qualifiedByName = "mappingTranslators")
    BookDetailServiceResponse bookToDetailServiceResponse(Book book);

    BookDetailServiceResponse bookSearchDetailToDetailServiceResponse(
            BookSearchDetailResponse bookSearchDetailResponse);

    @Mapping(target = "publicationDate", source = "publicationDate", qualifiedByName = "stringToLocalDateTime")
    BookCreateServiceRequest bookSearchDetailToBookCreateServiceRequest(
            BookSearchDetailResponse bookSearchDetailResponse);

    @Named("mappingAuthors")
    static List<BookDetailServiceResponse.Author> mappingAuthors(List<BookAuthor> bookAuthors) {
        return bookAuthors.stream()
                .map(BookAuthor::getAuthor)
                .map(author -> BookDetailServiceResponse.Author.builder()
                        .name(author.getName())
                        .authorId(author.getAid())
                        .build()
                ).toList();
    }

    @Named("mappingTranslators")
    static List<BookDetailServiceResponse.Translator> mappingTranslators(List<BookTranslator> bookTranslators) {
        return bookTranslators.stream()
                .map(BookTranslator::getTranslator)
                .map(translator -> BookDetailServiceResponse.Translator.builder()
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
}