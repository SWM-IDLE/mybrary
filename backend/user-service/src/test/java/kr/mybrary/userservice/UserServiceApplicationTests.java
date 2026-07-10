package kr.mybrary.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(
        initializers = {MysqlTestContainerConfig.class, RedisTestContainerConfig.class}
)
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
