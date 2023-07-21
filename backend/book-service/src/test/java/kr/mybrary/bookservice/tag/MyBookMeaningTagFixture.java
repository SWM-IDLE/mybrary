package kr.mybrary.bookservice.tag;

import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MyBookMeaningTagFixture {

    COMMON_MY_BOOK_MEANING_TAG(1L, MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook(), MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag(), "#FFFFFF");

    private final Long id;

    private final MyBook myBook;

    private final MeaningTag meaningTag;

    private final String meaningTagColor;


    public MyBookMeaningTag getMyBookMeaningTag() {
        return MyBookMeaningTag.builder()
                .id(id)
                .myBook(myBook)
                .meaningTag(meaningTag)
                .meaningTagColor(meaningTagColor)
                .build();
    }

    public MyBookMeaningTag getMyBookMeaningTag(MyBook myBook, MeaningTag meaningTag) {
        return MyBookMeaningTag.builder()
                .id(id)
                .myBook(myBook)
                .meaningTag(meaningTag)
                .meaningTagColor(meaningTagColor)
                .build();
    }
}
