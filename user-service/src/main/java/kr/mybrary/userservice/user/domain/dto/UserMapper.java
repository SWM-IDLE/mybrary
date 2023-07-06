package kr.mybrary.userservice.user.domain.dto;

import kr.mybrary.userservice.user.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.presentation.dto.response.SignUpResponse;
import kr.mybrary.userservice.user.persistence.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(SignUpRequest signUpRequest);

    SignUpResponse toResponse(User user);
}
