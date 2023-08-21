package kr.mybrary.userservice.interest.persistence;

import jakarta.persistence.*;
import kr.mybrary.userservice.global.BaseEntity;
import lombok.*;

@Entity
@Table(name = "interests")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_category_id")
    private InterestCategory category;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private int code;

    public void updateCategory(InterestCategory category) {
        this.category = category;
        category.getInterests().add(this);
    }

}
