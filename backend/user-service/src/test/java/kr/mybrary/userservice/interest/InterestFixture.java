package kr.mybrary.userservice.interest;

import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InterestFixture {

    DOMESTIC_NOVEL(1L, null, "국내소설", 50917),
    FOREIGN_NOVEL(2L, null, "외국소설", 50925),
    POEM(3L, null, "시", 50940),
    DRAMA(4L, null, "희곡", 50948),
    ESSAY(5L,null, "에세이", 55889),
    GENRE_FICTION(6L, null, "장르소설", 112011),

    SELF_IMPROVEMENT(7L, null, "자기계발", 336),
    SCIENCE(8L, null, "과학",  987),
    ENGINEERING(9L, null, "공학", 51038),
    IT(10L, null, "IT", 351),
    ECONOMIC_MANAGEMENT(11L, null, "경제경영", 170),
    SOCIAL_SCIENCE(12L, null, "사회과학", 798),
    HUMANITIES(13L, null, "인문학",  656),
    PSYCHOLOGY(14L, null, "심리학", 51395),

    KOREAN_HISTORY(15L, null, "한국사", 52593),
    WORLD_HISTORY(16L, null, "세계사", 169),
    CLASSIC(17L, null, "고전", 2105),
    RELIGION(18L, null, "종교", 1237),

    COOKING(19L, null, "요리", 53476),
    BEAUTY(20L, null, "뷰티", 53488),
    HEALTH(21L, null, "건강", 53521),
    SPORTS(22L, null, "스포츠", 53527),
    TRAVEL(23L, null, "여행", 1196),
    ART(24L, null, "예술", 517),
    FOREIGN_LANGUAGE(25L, null, "외국어", 1322),
    COMIC(26L, null, "만화", 2551),
    MAGAZINE(27L, null, "잡지", 2913),

    HOME(28L, null, "가정", 53489),
    GOOD_PARENTS(29L, null, "좋은부모", 2030),
    CHILD(30L, null, "유아", 13789);


    private final Long id;
    private final InterestCategory category;
    private final String name;
    private final int code;

    public Interest getInterest() {
        return Interest.builder()
                .id(id)
                .category(category)
                .name(name)
                .code(code)
                .build();
    }

}
