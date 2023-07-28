package kr.mybrary.bookservice.booksearch.domain.dto;

import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.dto.response.BookSearchResultServiceResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse.Author;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse.Translator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookSearchDtoMapper {

    String NOT_PROVIDED_PHRASES = "제공되지 않습니다.";

    BookSearchDtoMapper INSTANCE = Mappers.getMapper(BookSearchDtoMapper.class);

    @Mapping(target = "description", source = "contents")
    @Mapping(target = "author", expression = "java(String.join(\",\", kakaoBookSearchResponse.getAuthors()))")
    @Mapping(target = "isbn13", source = "isbn", qualifiedByName = "getISBN13")
    @Mapping(target = "thumbnailUrl", source = "thumbnail")
    @Mapping(target = "publicationDate", expression = "java(kakaoBookSearchResponse.getDatetime().split(\"T\")[0])")
    @Mapping(target = "starRating", constant = "0.0")
    BookSearchResultServiceResponse kakaoSearchResponseToServiceResponse(KakaoBookSearchResponse.Document kakaoBookSearchResponse);

    @Mapping(target = "thumbnailUrl", source = "cover")
    @Mapping(target = "publicationDate", source = "pubDate")
    @Mapping(target = "starRating", expression = "java(aladinBookSearchResponse.getCustomerReviewRank() / 2.0)")
    BookSearchResultServiceResponse aladinSearchResponseToServiceResponse(
            AladinBookSearchResponse.Item aladinBookSearchResponse);

    @Mapping(target = "subTitle", constant = NOT_PROVIDED_PHRASES)
    @Mapping(target = "link", source = "url")
    @Mapping(target = "isbn10", source = "isbn", qualifiedByName = "getISBN10")
    @Mapping(target = "isbn13", source = "isbn", qualifiedByName = "getISBN13")
    @Mapping(target = "authors",  source = "authors", qualifiedByName = "mappingAuthorNames")
    @Mapping(target = "translators",  source = "translators", qualifiedByName = "mappingTranslatorNames")
    @Mapping(target = "starRating", constant = "0.0")
    @Mapping(target = "reviewCount", constant = "0")
    @Mapping(target = "publicationDate", source = "datetime")
    @Mapping(target = "category", constant = NOT_PROVIDED_PHRASES)
    @Mapping(target = "categoryId", constant = "0")
    @Mapping(target = "pages", constant = "0")
    @Mapping(target = "description", source = "contents")
    @Mapping(target = "toc", constant = NOT_PROVIDED_PHRASES)
    @Mapping(target = "weight", constant = "0")
    @Mapping(target = "sizeDepth", constant = "0")
    @Mapping(target = "sizeHeight", constant = "0")
    @Mapping(target = "sizeWidth", constant = "0")
    @Mapping(target = "priceSales", source = "sale_price")
    @Mapping(target = "priceStandard", source = "price")
    BookSearchDetailResponse kakaoSearchResponseToDetailResponse(KakaoBookSearchResponse.Document kakaoBookSearchResponse);


    BookSearchDetailResponse aladinSearchResponseToDetailResponse(AladinBookSearchDetailResponse.Item aladinBookSearchResponse);

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

    @Named("mappingAuthorNames")
    static List<Author> mappingAuthorNames(List<String> authors) {
        return authors.stream()
                .map(name -> Author.builder()
                        .name(name)
                        .authorId(0)
                        .build())
                .toList();
    }

    @Named("mappingTranslatorNames")
    static List<Translator> mappingTranslatorNames(List<String> authors) {
        return authors.stream()
                .map(name -> Translator.builder()
                        .name(name)
                        .translatorId(0)
                        .build())
                .toList();
    }
}
