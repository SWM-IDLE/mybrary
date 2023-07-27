package kr.mybrary.bookservice.booksearch.domain.dto;

import kr.mybrary.bookservice.booksearch.domain.dto.response.BookSearchResultServiceResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookSearchDtoMapper {

    BookSearchDtoMapper INSTANCE = Mappers.getMapper(BookSearchDtoMapper.class);

    @Mapping(target = "description", source = "contents")
    @Mapping(target = "author", expression = "java(String.join(\",\", kakaoBookSearchResponse.getAuthors()))")
    @Mapping(target = "isbn13", source = "isbn", qualifiedByName = "getISBN13")
    @Mapping(target = "thumbnailUrl", source = "thumbnail")
    @Mapping(target = "publicationDate", source = "datetime")
    @Mapping(target = "starRating", constant = "0.0")
    BookSearchResultServiceResponse kakaoSearchResponseToServiceResponse(KakaoBookSearchResponse.Document kakaoBookSearchResponse);

    @Mapping(target = "thumbnailUrl", source = "cover")
    @Mapping(target = "publicationDate", source = "pubDate")
    @Mapping(target = "starRating", expression = "java(aladinBookSearchResponse.getCustomerReviewRank() / 2.0)")
    BookSearchResultServiceResponse aladinSearchResponseToServiceResponse(AladinBookSearchResponse.Item aladinBookSearchResponse);

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
