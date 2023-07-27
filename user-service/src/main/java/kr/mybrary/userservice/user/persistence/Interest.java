package kr.mybrary.userservice.user.persistence;

import jakarta.persistence.*;
import kr.mybrary.userservice.global.BaseEntity;
import kr.mybrary.userservice.user.persistence.repository.UserInterest;
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

}
