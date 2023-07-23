package kr.mybrary.bookservice.tag.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import kr.mybrary.bookservice.global.BaseEntity;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBookMeaningTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MyBook myBook;

    @ManyToOne(fetch = FetchType.LAZY)
    private MeaningTag meaningTag;

    private String meaningTagColor;

    public void assignMeaningTag(MeaningTag meaningTag, String meaningTagColor) {
        this.meaningTag = meaningTag;
        this.meaningTagColor = meaningTagColor;
    }
}
