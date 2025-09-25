package dev.k8s.backend.repository;

import org.springframework.stereotype.Repository;

@Repository
public class HelloRepository {
    public String fetchMessage() {
        return "Hello, Spring Boot!";
    }
}
