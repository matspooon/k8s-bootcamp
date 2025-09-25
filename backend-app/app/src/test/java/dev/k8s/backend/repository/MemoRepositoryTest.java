package dev.k8s.backend.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import dev.k8s.backend.entity.Memo;

@SpringBootTest
@Testcontainers
public class MemoRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(MemoRepositoryTest.class);

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("ki8stestdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    MemoRepository memoRepository;

    // Spring Boot가 application.properties 대신 컨테이너 값을 사용하도록 매핑
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    void containerStartsAndCreatesDatabase() {
        assertTrue(postgres.isRunning());
    }

    @Test
    public void testSave() {
        Memo memo = new Memo();
        memo.setTitle("Test Title");
        memo.setMemo("Test Memo");
        memoRepository.save(memo);
        logger.info("memoRepository size: " + memoRepository.findAll().size());
        assertEquals(1, memoRepository.findAll().size());

        memoRepository.findById(1L).ifPresent(foundMemo -> {
            assertEquals("Test Title", foundMemo.getTitle());
            assertEquals("Test Memo", foundMemo.getMemo());
            assertNotNull(foundMemo.getCreateDate());
            assertNull(foundMemo.getUpdateDate());
            logger.info("Create Date: " + foundMemo.getCreateDate());
            foundMemo.setTitle("updated title");
            memoRepository.save(foundMemo);
        });
        memoRepository.findById(1L).ifPresent(updatedMemo -> {
            assertEquals("updated title", updatedMemo.getTitle());
            assertNotNull(updatedMemo.getUpdateDate());
            logger.info("Update Date: " + updatedMemo.getUpdateDate());
        });
    }
}
