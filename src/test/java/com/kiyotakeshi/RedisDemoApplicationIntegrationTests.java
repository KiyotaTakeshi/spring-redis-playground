package com.kiyotakeshi;

import com.kiyotakeshi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class RedisDemoApplicationIntegrationTests {

    private static final String KEY = "USER";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Container
    private static final GenericContainer REDIS = new GenericContainer(DockerImageName.parse("redis:6.2.6-alpine")).withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        REDIS.start();
        registry.add("spring.redis.host", REDIS::getContainerIpAddress);
        registry.add("spring.redis.port", REDIS::getFirstMappedPort);
    }

    private String getTestBaseUrl() {
        return "http://localhost:" + port + "/users";
    }

    @BeforeEach
    void setUp() {
        redisTemplate.delete(KEY);
        Map<String, User> map = new HashMap<String, User>();
        User user = new User(1L, "yamada", "taro", "yamada.taro@example.com", 22);
        User user2 = new User(3L, "suzuki", "jiro", "suzuki.jiro@example.com", 32);
        User user3 = new User(2L, "tanaka", "ichiro", "tanaka.ichiro@example.com", 24);
        map.put("1", user);
        map.put("2", user2);
        map.put("3", user3);
        redisTemplate.opsForHash().putAll(KEY, map);
    }

    @Test
    void saveUser() throws Exception {
        var user = new User(4L, "takahashi", "shiro", "takahashi.shiro@example.com", 28);

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(getTestBaseUrl(), user, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Created Successfully!!", response.getBody());

        var result = (User) redisTemplate.opsForHash().get(KEY, user.getId().toString());
        assertEquals(user.getFirstName(), result.getFirstName());
    }

    @Test
    void saveUserFailure() throws Exception {
        var user = new User(1L, "tanaka", "taro", "yamada.taro@example.com", 22);

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(getTestBaseUrl(), user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("check UserCompare Comparator works as expect")
    void fetchAllUser() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(getTestBaseUrl(), String.class);

        String expected = """
                [{"id":1,"firstName":"yamada","lastName":"taro","email":"yamada.taro@example.com","age":22},{"id":2,"firstName":"tanaka","lastName":"ichiro","email":"tanaka.ichiro@example.com","age":24},{"id":3,"firstName":"suzuki","lastName":"jiro","email":"suzuki.jiro@example.com","age":32}]""";
        assertEquals(expected,response.getBody());
    }

    // TODO:
    @Test
    void deleteUser() {
    }

    // TODO:
    @Test
    void updateUser() {
    }
}
