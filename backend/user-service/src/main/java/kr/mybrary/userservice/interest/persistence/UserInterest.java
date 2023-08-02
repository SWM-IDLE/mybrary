package kr.mybrary.userservice.interest.persistence;

import jakarta.persistence.*;
import kr.mybrary.userservice.global.BaseEntity;
import kr.mybrary.userservice.user.persistence.User;
import lombok.*;

@Entity
@Table(name = "users_interests",
    uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_users_interests_user_id_interest_id",
                columnNames = {"user_id", "interest_id"}
        )
    }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInterest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id")
    private Interest interest;

}
