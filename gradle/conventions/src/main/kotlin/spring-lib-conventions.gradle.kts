plugins {
  id("java-library")
  id("io.spring.dependency-management")
}

repositories {
  mavenCentral()
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17)) // Java 17
  }
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"  // UTF-8 인코딩
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  api("org.springframework.boot:spring-boot-starter")
  api("org.springframework.boot:spring-boot-starter-web")
  api("org.springframework.boot:spring-boot-starter-data-jpa")
  api("org.springframework.boot:spring-boot-starter-validation")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.boot:spring-boot-dependencies:2.7.14")
  }
}
