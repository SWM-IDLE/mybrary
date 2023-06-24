package kr.mybrary.bookservice.book.domain.dto;

import kr.mybrary.bookservice.book.domain.dto.kakaoapi.Document;
import kr.mybrary.bookservice.book.presentation.dto.response.BookSearchResultResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookDtoMapper {

    BookDtoMapper INSTANCE = Mappers.getMapper(BookDtoMapper.class);

    @Mapping(target = "thumbnailUrl", source = "thumbnail")
    @Mapping(target = "publicationDate", source = "datetime")
    @Mapping(target = "starRating", constant = "0.0")
    BookSearchResultResponse kakaoSearchResponseToResponseDto(Document kakaoBookSearchResponse);
}
