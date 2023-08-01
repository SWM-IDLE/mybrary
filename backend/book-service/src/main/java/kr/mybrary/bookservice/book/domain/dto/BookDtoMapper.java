package kr.mybrary.bookservice.book.domain.dto;

import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
}