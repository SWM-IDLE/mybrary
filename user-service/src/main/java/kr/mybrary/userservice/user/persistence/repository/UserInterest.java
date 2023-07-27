package kr.mybrary.userservice.user.persistence.repository;

import jakarta.persistence.*;
import kr.mybrary.userservice.global.BaseEntity;
import kr.mybrary.userservice.user.persistence.Interest;
import kr.mybrary.userservice.user.persistence.User;
import lombok.*;

@Entity
@Table(name = "users_interests")
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
