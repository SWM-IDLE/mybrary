package kr.mybrary.userservice.interest.domain.dto;

import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserInterestMapper {

    UserInterestMapper INSTANCE = Mappers.getMapper(UserInterestMapper.class);

    @Mapping(target = "name", source = "interest.name")
    @Mapping(target = "id", source = "interest.id")
    InterestResponse toInterestResponse(UserInterest userInterest);
}
