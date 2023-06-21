package kr.mybrary.userservice.authentication.application.dto;

import kr.mybrary.userservice.authentication.presentation.dto.request.SignUpRequest;
import kr.mybrary.userservice.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(SignUpRequest signUpRequest);

    // SignUpResponse toResponse(User user);
}
