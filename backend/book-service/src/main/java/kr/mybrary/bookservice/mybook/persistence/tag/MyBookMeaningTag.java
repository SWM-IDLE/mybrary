package kr.mybrary.bookservice.mybook.persistence.tag;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class MyBookMeaningTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MyBook myBook;

    @OneToOne(fetch = FetchType.LAZY)
    private MeaningTag meaningTag;

    @Enumerated(EnumType.STRING)
    private QuoteColor quoteColor;
}
