package kr.mybrary.userservice.user.persistence;

import jakarta.persistence.*;
import java.util.List;
import kr.mybrary.userservice.global.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_login_id_deleted",
                        columnNames = {"loginId", "deleted"}
                ),
                @UniqueConstraint(
                        name = "uk_users_nickname_deleted",
                        columnNames = {"nickname", "deleted"}
                ),
                @UniqueConstraint(
                        name = "uk_users_login_id_nickname_deleted",
                        columnNames = {"loginId", "nickname", "deleted"}
                ),
        }
)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String email;

    private String introduction;

    private String profileImageUrl;

    private String profileImageThumbnailTinyUrl;

    private String profileImageThumbnailSmallUrl;

    @OneToMany(mappedBy = "target", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "source", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Follow> followings;

    // 주소, 직장, 직장 공개 여부, 성별, 생년월일, 학력, 본인인증 여부 추가 예정

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfile(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfileImageThumbnailTinyUrl(String profileImageThumbnailTinyUrl) {
        this.profileImageThumbnailTinyUrl = profileImageThumbnailTinyUrl;
    }

    public void updateProfileImageThumbnailSmallUrl(String profileImageThumbnailSmallUrl) {
        this.profileImageThumbnailSmallUrl = profileImageThumbnailSmallUrl;
    }

    public void follow(User target) {
        Follow follow = Follow.builder()
            .source(this)
            .target(target)
            .build();
        this.followings.add(follow);
        target.followers.add(follow);
    }

    // TODO: 팔로우를 soft delete 해야하는지 고민해보기
    public void unfollow(User target) {
         this.followings.removeIf(follow -> follow.getTarget().equals(target));
         target.followers.removeIf(follow -> follow.getSource().equals(this));
    }

}
