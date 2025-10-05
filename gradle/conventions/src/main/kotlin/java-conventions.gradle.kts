plugins {
  `java-library`
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
  implementation("org.apache.commons:commons-lang3:3.19.0")
  implementation("org.jodd:jodd-util:6.3.0") // jakrata commons beanutils 대체
  
  // Lombok
  compileOnly("org.projectlombok:lombok:1.18.30")
  annotationProcessor("org.projectlombok:lombok:1.18.30")

  testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}