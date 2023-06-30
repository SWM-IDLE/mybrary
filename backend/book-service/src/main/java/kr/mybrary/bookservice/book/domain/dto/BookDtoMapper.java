package kr.mybrary.bookservice.book.domain.dto;

import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookDtoMapper {

    BookDtoMapper INSTANCE = Mappers.getMapper(BookDtoMapper.class);

    @Mapping(target = "publishDate", source = "publicationDate")
    Book bookCreateRequestToEntity(BookCreateRequest request);
}