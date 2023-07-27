package kr.mybrary.userservice.interest;

import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum InterestFixture {

    DOMESTIC_NOVEL(1L, InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory(), "국내소설", null),
    FOREIGN_NOVEL(2L, InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory(), "외국소설", null),
    POEM(3L, InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory(), "시", null),
    DRAMA(4L, InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory(), "희곡", null),
    ESSAY(5L, InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory(), "에세이", null),
    GENRE_FICTION(6L, InterestCategoryFixture.LITERATURE_SENSIBILITY.getInterestCategory(), "장르소설", null),

    SELF_IMPROVEMENT(7L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "자기계발", null),
    SCIENCE(8L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "과학", null),
    ENGINEERING(9L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "공학", null),
    IT(10L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "IT", null),
    ECONOMIC_MANAGEMENT(11L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "경제경영", null),
    SOCIAL_SCIENCE(12L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "사회과학", null),
    HUMANITIES(13L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "인문학", null),
    PSYCHOLOGY(14L, InterestCategoryFixture.LEARNING_GROWTH.getInterestCategory(), "심리학", null),

    KOREAN_HISTORY(15L, InterestCategoryFixture.CULTURE_HISTORY.getInterestCategory(), "한국사", null),
    WORLD_HISTORY(16L, InterestCategoryFixture.CULTURE_HISTORY.getInterestCategory(), "세계사", null),
    CLASSIC(17L, InterestCategoryFixture.CULTURE_HISTORY.getInterestCategory(), "고전", null),
    RELIGION(18L, InterestCategoryFixture.CULTURE_HISTORY.getInterestCategory(), "종교", null),

    COOKING(19L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "요리", null),
    BEAUTY(20L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "뷰티", null),
    HEALTH(21L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "건강", null),
    SPORTS(22L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "스포츠", null),
    TRAVEL(23L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "여행", null),
    ART(24L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "예술", null),
    FOREIGN_LANGUAGE(25L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "외국어", null),
    COMIC(26L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "만화", null),
    MAGAZINE(27L, InterestCategoryFixture.LEISURE_HOBBY.getInterestCategory(), "잡지", null),

    HOME(28L, InterestCategoryFixture.HOME.getInterestCategory(), "가정", null),
    GOOD_PARENTS(29L, InterestCategoryFixture.HOME.getInterestCategory(), "좋은부모", null),
    CHILD(30L, InterestCategoryFixture.HOME.getInterestCategory(), "유아", null);


    private final Long id;
    private final InterestCategory category;
    private final String name;
    private final List<UserInterest> registeredUsers;

    public Interest getInterest() {
        return Interest.builder()
                .id(id)
                .category(category)
                .name(name)
                .registeredUsers(registeredUsers)
                .build();
    }

    public  Interest.InterestBuilder getInterestBuilder() {
        return Interest.builder()
                .id(id)
                .category(category)
                .name(name)
                .registeredUsers(registeredUsers);
    }
}
