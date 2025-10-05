plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
}

dependencies {
  // spring veriosn 명시 : precompile되는 springboot-conventions.gradle.kts에는 버젼을 명시할 수 없다
  implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:2.7.14")
  implementation("io.spring.gradle:dependency-management-plugin:1.0.15.RELEASE")
}