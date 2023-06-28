package kr.mybrary.bookservice.book.domain.dto;

import kr.mybrary.bookservice.book.domain.dto.kakaoapi.Document;
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
    BookSearchResultDto kakaoSearchResponseToDto(Document kakaoBookSearchResponse);
}
