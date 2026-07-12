package kr.mybrary.review.domain.dto;

import java.time.LocalDateTime;
import kr.mybrary.global.util.DateUtils;
import kr.mybrary.review.persistence.model.MyReviewFromMyBookModel;
import kr.mybrary.review.presentation.dto.response.MyReviewOfMyBookGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyReviewDtoMapper {


    MyReviewDtoMapper INSTANCE = Mappers.getMapper(MyReviewDtoMapper.class);


    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toFormatMyBookReviewUI")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toFormatMyBookReviewUI")
    MyReviewOfMyBookGetResponse reviewOfMyBookModelToResponse(MyReviewFromMyBookModel reviewOfMyBookGetModel);

    @Named("toFormatMyBookReviewUI")
    static String toFormatMyBookReviewUI(LocalDateTime dateTime) {
        return DateUtils.toDotFormatYYYYMMDD(dateTime);
    }
}
