package kr.mybrary.bookservice.mybook.domain.dto;

import java.util.List;
import kr.mybrary.bookservice.book.persistence.author.BookAuthor;
import kr.mybrary.bookservice.book.persistence.translator.BookTranslator;
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
}
