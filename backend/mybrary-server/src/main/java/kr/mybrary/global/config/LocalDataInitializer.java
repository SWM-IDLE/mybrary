package kr.mybrary.global.config;

import kr.mybrary.user.persistence.Role;
import kr.mybrary.user.persistence.User;
import kr.mybrary.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class LocalDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_LOGIN_ID = "admin@mybrary.kr";
    private static final String ADMIN_NICKNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin1234!";

    @Override
    public void run(String... args) {
        if (!userRepository.existsByLoginId(ADMIN_LOGIN_ID)) {
            User admin = User.builder()
                    .loginId(ADMIN_LOGIN_ID)
                    .nickname(ADMIN_NICKNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(Role.ADMIN)
                    .introduction("로컬 개발용 어드민 계정")
                    .profileImageUrl("")
                    .profileImageThumbnailTinyUrl("")
                    .profileImageThumbnailSmallUrl("")
                    .build();
            userRepository.save(admin);
            log.info("===== [LOCAL] 어드민 계정 생성 =====");
            log.info("ID: {}", ADMIN_LOGIN_ID);
            log.info("PW: {}", ADMIN_PASSWORD);
            log.info("===================================");
        }
    }
}
