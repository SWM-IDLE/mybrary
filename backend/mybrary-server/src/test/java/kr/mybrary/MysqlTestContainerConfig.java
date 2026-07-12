package kr.mybrary;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MysqlTestContainerConfig
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    // JVM 내 모든 테스트가 동일 컨테이너를 공유 (기동 비용 최소화)
    static final MySQLContainer<?> MYSQL =
            new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                    .withDatabaseName("mybrary_test")
                    .withUsername("test")
                    .withPassword("test");

    static {
        MYSQL.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                "spring.datasource.url=" + MYSQL.getJdbcUrl(),
                "spring.datasource.username=" + MYSQL.getUsername(),
                "spring.datasource.password=" + MYSQL.getPassword(),
                "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
                "spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect"
        ).applyTo(ctx.getEnvironment());
    }
}
