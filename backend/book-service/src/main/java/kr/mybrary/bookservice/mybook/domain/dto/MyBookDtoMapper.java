package kr.mybrary.bookservice.mybook.domain.dto;

import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyBookDtoMapper {

    MyBookDtoMapper INSTANCE = Mappers.getMapper(MyBookDtoMapper.class);

    @Mapping(target = "book.stars", constant = "0.0")
    MyBookElementResponse entityToMyBookElementResponse(MyBook myBook);
}
