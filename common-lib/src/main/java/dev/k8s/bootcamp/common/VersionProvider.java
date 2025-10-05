package dev.k8s.bootcamp.common;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VersionProvider {
  private Version cachedVersion;

  @PostConstruct
  public void init() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    try (InputStream is = new ClassPathResource("version.json").getInputStream()) {
      this.cachedVersion = mapper.readValue(is, Version.class);
    }
  }

  public Version getVersion() {
    return cachedVersion;
  }
}
