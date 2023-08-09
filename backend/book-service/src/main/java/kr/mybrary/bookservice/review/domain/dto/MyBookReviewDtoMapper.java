package kr.mybrary.bookservice.review.domain.dto;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.global.util.DateUtils;
import kr.mybrary.bookservice.review.persistence.model.ReviewFromMyBookModel;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewOfMyBookGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyBookReviewDtoMapper {


    MyBookReviewDtoMapper INSTANCE = Mappers.getMapper(MyBookReviewDtoMapper.class);


    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toFormatMyBookReviewUI")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toFormatMyBookReviewUI")
    ReviewOfMyBookGetResponse reviewOfMyBookModelToResponse(ReviewFromMyBookModel reviewOfMyBookGetModel);

    @Named("toFormatMyBookReviewUI")
    static String toFormatMyBookReviewUI(LocalDateTime dateTime) {
        return DateUtils.toFormatMyBookReviewUI(dateTime);
    }
}
