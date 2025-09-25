package dev.k8s.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.k8s.backend.common.Version;
import dev.k8s.backend.common.VersionProvider;

@RestController
public class HealthCheckController {
    private final VersionProvider versionProvider;

    public HealthCheckController(VersionProvider versionProvider) {
        this.versionProvider = versionProvider;
    }

    @GetMapping("/healthcheck")
    public Version hello() {
        return versionProvider.getVersion();
    }
}
