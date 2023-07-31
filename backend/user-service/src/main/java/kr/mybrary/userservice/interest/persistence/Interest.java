package kr.mybrary.userservice.interest.persistence;

import jakarta.persistence.*;
import kr.mybrary.userservice.global.BaseEntity;
import kr.mybrary.userservice.user.persistence.User;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "interest", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserInterest> registeredUsers;

    public void addRegisteredUser(User user) {
        this.registeredUsers.add(UserInterest.builder()
                .user(user)
                .interest(this)
                .build());
    }

    public void updateCategory(InterestCategory category) {
        this.category = category;
        category.getInterests().add(this);
    }

}
