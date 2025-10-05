plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  application
  java
}

repositories {
    mavenCentral()
}

dependencies {
  // spring boot
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-actuator") // spring actuator : 모니터링

  // db : postgresql
  runtimeOnly("org.postgresql:postgresql")

  // Swagger Restful API 문서 자동화
  implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

  // local springboot 실행시 env load용 
  implementation("io.github.cdimascio:dotenv-java:3.2.0")

  // test 
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "junit", module = "junit")  // junit4 제외
    exclude(group = "org.junit.jupiter")  // junit5.8 버젼 충돌
    exclude(group = "org.junit.platform")  // junit5.8 버젼 충돌
    exclude(group = "org.junit.vintage") // junit5.8 버젼 충돌
  }
  testImplementation("com.ninja-squad:DbSetup:2.1.0")
  testImplementation("org.assertj:assertj-db:1.3.0")

  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")   // 최신 JUnit5
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
  // jpa test container
  testImplementation("org.testcontainers:junit-jupiter:1.21.3")
  testImplementation("org.testcontainers:postgresql:1.21.3")
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.junit.jupiter") {
            useVersion("5.10.2")
        }
        if (requested.group == "org.junit.platform") {
            useVersion("1.10.2")
        }
    }
}

tasks.test {
  useJUnitPlatform()
}

