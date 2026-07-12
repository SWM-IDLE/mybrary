package kr.mybrary.user.domain.dto;

import kr.mybrary.user.domain.dto.request.SignUpServiceRequest;
import kr.mybrary.user.domain.dto.response.ProfileServiceResponse;
import kr.mybrary.user.domain.dto.response.SignUpServiceResponse;
import kr.mybrary.user.persistence.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "introduction", expression = "java(\"\")")
    User toEntity(SignUpServiceRequest serviceRequest);

    SignUpServiceResponse toSignUpServiceResponse(User user);

    ProfileServiceResponse toProfileServiceResponse(User user);
}
