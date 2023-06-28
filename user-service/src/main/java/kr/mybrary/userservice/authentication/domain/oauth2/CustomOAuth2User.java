package kr.mybrary.userservice.authentication.domain.oauth2;

import java.util.Collection;
import java.util.Map;
import kr.mybrary.userservice.user.persistence.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String loginId;
    private Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes, String nameAttributeKey, String loginId, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.loginId = loginId;
        this.role = role;
    }

}
