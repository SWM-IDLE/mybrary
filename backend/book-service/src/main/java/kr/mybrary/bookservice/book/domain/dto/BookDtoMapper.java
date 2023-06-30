package kr.mybrary.bookservice.book.domain.dto;

import kr.mybrary.bookservice.book.domain.dto.kakaoapi.Document;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookDtoMapper {

    BookDtoMapper INSTANCE = Mappers.getMapper(BookDtoMapper.class);

    @Mapping(target = "thumbnailUrl", source = "thumbnail")
    @Mapping(target = "publicationDate", source = "datetime")
    @Mapping(target = "detailsUrl", source = "url")
    @Mapping(target = "salePrice", source = "sale_price")
    @Mapping(target = "description", source = "contents")
    @Mapping(target = "starRating", constant = "0.0")
    @Mapping(target = "isbn10", source = "isbn", qualifiedByName = "getISBN10")
    @Mapping(target = "isbn13", source = "isbn", qualifiedByName = "getISBN13")
    BookSearchResultDto kakaoSearchResponseToDto(Document kakaoBookSearchResponse);

    @Mapping(target = "publishDate", source = "publicationDate")
    Book bookCreateRequestToEntity(BookCreateRequest request);

    @Named("getISBN10")
    static String getISBN10(String isbn) {
        if (isbn.length() == 10 || isbn.length() == 24) {
            return isbn.substring(0, 10);
        }
        return "";
    }

    @Named("getISBN13")
    static String getISBN13(String isbn) {
        if (isbn.length() == 13) {
            return isbn;
        } else if (isbn.length() == 24) {
            return isbn.substring(11, 24);
        }
        return "";
    }
}
