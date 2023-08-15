package kr.mybrary.bookservice.booksearch.domain.dto;

import java.util.List;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookListByCategoryResponseElement;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponseElement;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookListByCategorySearchResponse;
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
    BookSearchResultResponseElement kakaoSearchResponseToServiceResponse(
            KakaoBookSearchResponse.Document kakaoBookSearchResponse);

    @Mapping(target = "thumbnailUrl", source = "cover")
    @Mapping(target = "publicationDate", source = "pubDate")
    @Mapping(target = "starRating", expression = "java(aladinBookSearchResponse.getCustomerReviewRank() / 2.0)")
    BookSearchResultResponseElement aladinSearchResponseToServiceResponse(
            AladinBookSearchResponse.Item aladinBookSearchResponse);

    @Mapping(target = "subTitle", constant = NOT_PROVIDED_PHRASES)
    @Mapping(target = "link", source = "url")
    @Mapping(target = "isbn10", source = "isbn", qualifiedByName = "getISBN10")
    @Mapping(target = "isbn13", source = "isbn", qualifiedByName = "getISBN13")
    @Mapping(target = "author", expression = "java(String.join(\",\", kakaoBookSearchResponse.getAuthors()))")
    @Mapping(target = "authors", source = "authors", qualifiedByName = "mappingAuthorNames")
    @Mapping(target = "translators", source = "translators", qualifiedByName = "mappingTranslatorNames")
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
    @Mapping(target = "holderCount", constant = "0")
    @Mapping(target = "readCount", constant = "0")
    @Mapping(target = "interestCount", constant = "0")
    BookSearchDetailResponse kakaoSearchResponseToDetailResponse(
            KakaoBookSearchResponse.Document kakaoBookSearchResponse);

    @Mapping(target = "title", expression = "java(aladinBookSearchResponse.getTitle().replace(\" - \" + aladinBookSearchResponse.getSubInfo().getSubTitle(), \"\"))")
    @Mapping(target = "subTitle", source = "subInfo.subTitle")
    @Mapping(target = "thumbnail", source = "cover")
    @Mapping(target = "starRating", expression = "java(aladinBookSearchResponse.getCustomerReviewRank() / 2.0)")
    @Mapping(target = "reviewCount", source = "subInfo.ratingInfo.myReviewCount")
    @Mapping(target = "authors", source = "subInfo.authors", qualifiedByName = "mappingAuthorNamesAndIds")
    @Mapping(target = "translators", source = "subInfo.authors", qualifiedByName = "mappingTranslatorNamesAndIds")
    @Mapping(target = "publicationDate", source = "pubDate")
    @Mapping(target = "category", source = "categoryName")
    @Mapping(target = "pages", source = "subInfo.itemPage")
    @Mapping(target = "description", source = "fullDescription")
    @Mapping(target = "toc", source = "subInfo.toc")
    @Mapping(target = "isbn10", source = "isbn")
    @Mapping(target = "weight", source = "subInfo.packing.weight")
    @Mapping(target = "sizeDepth", source = "subInfo.packing.sizeDepth")
    @Mapping(target = "sizeHeight", source = "subInfo.packing.sizeHeight")
    @Mapping(target = "sizeWidth", source = "subInfo.packing.sizeWidth")
    @Mapping(target = "holderCount", constant = "0")
    @Mapping(target = "readCount", constant = "0")
    @Mapping(target = "interestCount", constant = "0")
    BookSearchDetailResponse aladinSearchResponseToDetailResponse(AladinBookSearchDetailResponse.Item aladinBookSearchResponse);


    @Mapping(target = "thumbnailUrl", source = "cover")
    BookListByCategoryResponseElement aladinBookListByCategorySearchResponseToServiceResponse(
            AladinBookListByCategorySearchResponse.Item aladinBookListByCategorySearchResponse);


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

    @Named("mappingAuthorNamesAndIds")
    static List<BookSearchDetailResponse.Author> mappingAuthorNamesAndIds(List<AladinBookSearchDetailResponse.Author> authors) {
        return authors.stream()
                .filter(author -> author.getAuthorType().equals("author"))
                .map(author -> BookSearchDetailResponse.Author.builder()
                        .name(author.getAuthorName())
                        .authorId(author.getAuthorId())
                        .build())
                .toList();
    }

    @Named("mappingTranslatorNamesAndIds")
    static List<BookSearchDetailResponse.Translator> mappingTranslatorNamesAndIds(List<AladinBookSearchDetailResponse.Author> authors) {
        return authors.stream()
                .filter(author -> author.getAuthorType().equals("translator"))
                .map(author -> BookSearchDetailResponse.Translator.builder()
                        .name(author.getAuthorName())
                        .translatorId(author.getAuthorId())
                        .build())
                .toList();
    }
}
