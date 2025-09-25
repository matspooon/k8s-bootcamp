package dev.k8s.backend.service;

import org.springframework.stereotype.Service;

import dev.k8s.backend.repository.HelloRepository;

@Service
public class HelloService {
    private final HelloRepository helloRepository;

    public HelloService(HelloRepository helloRepository) {
        this.helloRepository = helloRepository;
    }

    public String getHelloMessage() {
        return helloRepository.fetchMessage();
    }
}
