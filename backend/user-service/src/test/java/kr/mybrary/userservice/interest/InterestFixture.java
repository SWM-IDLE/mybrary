package kr.mybrary.userservice.interest;

import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum InterestFixture {

    DOMESTIC_NOVEL(1L, null, "국내소설"),
    FOREIGN_NOVEL(2L, null, "외국소설"),
    POEM(3L, null, "시"),
    DRAMA(4L, null, "희곡"),
    ESSAY(5L,null, "에세이"),
    GENRE_FICTION(6L, null, "장르소설"),

    SELF_IMPROVEMENT(7L, null, "자기계발"),
    SCIENCE(8L, null, "과학"),
    ENGINEERING(9L, null, "공학"),
    IT(10L, null, "IT"),
    ECONOMIC_MANAGEMENT(11L, null, "경제경영"),
    SOCIAL_SCIENCE(12L, null, "사회과학"),
    HUMANITIES(13L, null, "인문학"),
    PSYCHOLOGY(14L, null, "심리학"),

    KOREAN_HISTORY(15L, null, "한국사"),
    WORLD_HISTORY(16L, null, "세계사"),
    CLASSIC(17L, null, "고전"),
    RELIGION(18L, null, "종교"),

    COOKING(19L, null, "요리"),
    BEAUTY(20L, null, "뷰티"),
    HEALTH(21L, null, "건강"),
    SPORTS(22L, null, "스포츠"),
    TRAVEL(23L, null, "여행"),
    ART(24L, null, "예술"),
    FOREIGN_LANGUAGE(25L, null, "외국어"),
    COMIC(26L, null, "만화"),
    MAGAZINE(27L, null, "잡지"),

    HOME(28L, null, "가정"),
    GOOD_PARENTS(29L, null, "좋은부모"),
    CHILD(30L, null, "유아");


    private final Long id;
    private final InterestCategory category;
    private final String name;

    public Interest getInterest() {
        return Interest.builder()
                .id(id)
                .category(category)
                .name(name)
                .build();
    }

}
