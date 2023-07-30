package kr.mybrary.userservice.interest.domain.dto;

import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InterestCategoryMapper {

    InterestCategoryMapper INSTANCE = Mappers.getMapper(InterestCategoryMapper.class);

    @Mapping(target = "interestResponses", source = "interests", qualifiedByName = "getInterestResponses")
    InterestCategoryResponse toInterestCategoryResponse(InterestCategory interestCategory);

    @Named("getInterestResponses")
    static List<InterestResponse> getInterestResponses(List<Interest> interests) {
        return interests.stream()
                .map(interest -> InterestResponse.builder()
                        .id(interest.getId())
                        .name(interest.getName())
                        .build())
                .toList();
    }

}
