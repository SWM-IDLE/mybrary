package kr.mybrary.userservice.interest.persistence;

import jakarta.persistence.*;
import kr.mybrary.userservice.global.BaseEntity;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "interest_categories")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InterestCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Interest> interests;

}
