package kr.mybrary.userservice.interest.domain.dto;

import kr.mybrary.userservice.interest.InterestCategoryFixture;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InterestCategoryMapperTest {

    @Test
    @DisplayName("관심 카테고리 엔티티를 관심 카테고리 응답 DTO로 변환한다.")
    void toInterestCategoryResponse() {
        // given
        InterestCategory interestCategory = InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory();

        // when
        InterestCategoryResponse serviceResponse = InterestCategoryMapper.INSTANCE.toInterestCategoryResponse(interestCategory);

        // then
        assertAll(
                () -> assertEquals(interestCategory.getId(), serviceResponse.getId()),
                () -> assertEquals(interestCategory.getName(), serviceResponse.getName()),
                () -> assertEquals(interestCategory.getDescription(), serviceResponse.getDescription()),
                () -> assertEquals(interestCategory.getInterests().size(), serviceResponse.getInterestResponses().size()),
                () -> assertThat(serviceResponse.getInterestResponses()).extracting("id").containsExactlyElementsOf(
                        interestCategory.getInterests().stream().map(interest -> interest.getId()).collect(Collectors.toList())),
                () -> assertThat(serviceResponse.getInterestResponses()).extracting("name").containsExactlyElementsOf(
                        interestCategory.getInterests().stream().map(interest -> interest.getName()).collect(Collectors.toList()))
        );
    }

    @Test
    @DisplayName("관심 카테고리 엔티티의 관심사 리스트를 관심 카테고리 응답 DTO의 관심사 응답 리스트로 변환한다.")
    void toInterestCategoryServiceResponse() {
        // given
        InterestCategory interestCategory = InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory();

        // when
        List<InterestResponse> interestResponses = InterestCategoryMapper.getInterestResponses(interestCategory.getInterests());

        // then
        assertAll(
                () -> assertEquals(interestCategory.getInterests().size(), interestResponses.size()),
                () -> assertEquals(interestCategory.getInterests().get(0).getId(), interestResponses.get(0).getId()),
                () -> assertEquals(interestCategory.getInterests().get(0).getName(), interestResponses.get(0).getName()),
                () -> assertEquals(interestCategory.getInterests().get(1).getId(), interestResponses.get(1).getId()),
                () -> assertEquals(interestCategory.getInterests().get(1).getName(), interestResponses.get(1).getName()),
                () -> assertEquals(interestCategory.getInterests().get(2).getId(), interestResponses.get(2).getId()),
                () -> assertEquals(interestCategory.getInterests().get(2).getName(), interestResponses.get(2).getName())
        );
    }

}