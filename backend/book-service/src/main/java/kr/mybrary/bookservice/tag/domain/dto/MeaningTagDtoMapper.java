package kr.mybrary.bookservice.tag.domain.dto;


import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.presentation.dto.response.MeaningTagElementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeaningTagDtoMapper {

    MeaningTagDtoMapper INSTANCE = Mappers.getMapper(MeaningTagDtoMapper.class);

    MeaningTagElementResponse entityToMeaningTagElementResponse(MeaningTag meaningTag);
}
