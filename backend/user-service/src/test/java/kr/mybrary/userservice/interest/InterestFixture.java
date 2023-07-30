package kr.mybrary.userservice.interest;

import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum InterestFixture {

    DOMESTIC_NOVEL(1L, null, "국내소설", null),
    FOREIGN_NOVEL(2L, null, "외국소설", null),
    POEM(3L, null, "시", null),
    DRAMA(4L, null, "희곡", null),
    ESSAY(5L,null, "에세이", null),
    GENRE_FICTION(6L, null, "장르소설", null),

    SELF_IMPROVEMENT(7L, null, "자기계발", null),
    SCIENCE(8L, null, "과학", null),
    ENGINEERING(9L, null, "공학", null),
    IT(10L, null, "IT", null),
    ECONOMIC_MANAGEMENT(11L, null, "경제경영", null),
    SOCIAL_SCIENCE(12L, null, "사회과학", null),
    HUMANITIES(13L, null, "인문학", null),
    PSYCHOLOGY(14L, null, "심리학", null),

    KOREAN_HISTORY(15L, null, "한국사", null),
    WORLD_HISTORY(16L, null, "세계사", null),
    CLASSIC(17L, null, "고전", null),
    RELIGION(18L, null, "종교", null),

    COOKING(19L, null, "요리", null),
    BEAUTY(20L, null, "뷰티", null),
    HEALTH(21L, null, "건강", null),
    SPORTS(22L, null, "스포츠", null),
    TRAVEL(23L, null, "여행", null),
    ART(24L, null, "예술", null),
    FOREIGN_LANGUAGE(25L, null, "외국어", null),
    COMIC(26L, null, "만화", null),
    MAGAZINE(27L, null, "잡지", null),

    HOME(28L, null, "가정", null),
    GOOD_PARENTS(29L, null, "좋은부모", null),
    CHILD(30L, null, "유아", null);


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
