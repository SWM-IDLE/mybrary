package kr.mybrary.userservice.interest;

import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum InterestCategoryFixture {

    LITERATURE_SENSIBILITY(1L, "문학 감성", "문학 감성을 좋아하시나요?",
            List.of(InterestFixture.DOMESTIC_NOVEL.getInterest(), InterestFixture.FOREIGN_NOVEL.getInterest(), InterestFixture.POEM.getInterest())),
    LEARNING_GROWTH(2L, "학습 및 성장", "학습 및 성장을 갈망하고 계신가요?",
            List.of(InterestFixture.SELF_IMPROVEMENT.getInterest(), InterestFixture.SCIENCE.getInterest())),

    CULTURE_HISTORY(3L, "문화와 역사", "문화와 역사에 대한 관심도 좋아요.",
            List.of(InterestFixture.KOREAN_HISTORY.getInterest())),
    LEISURE_HOBBY(4L, "여가와 취미", "어떤 여가와 취미를 즐기시나요?",
            List.of(InterestFixture.COOKING.getInterest())),
    HOME(5L, "가정", "좋은 가정을 위해 고민해보세요.",
            List.of(InterestFixture.HOME.getInterest()));


    private final Long id;
    private final String name;
    private final String description;
    private final List<Interest> interests;

    public InterestCategory getInterestCategory() {
        return InterestCategory.builder()
                .id(id)
                .name(name)
                .description(description)
                .interests(interests)
                .build();
    }
}
