package kr.mybrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MybrayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybrayServerApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static {
        // AWS EC2 메타데이터 조회 비활성화 (로컬/테스트 환경에서 불필요한 대기 방지)
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
}
