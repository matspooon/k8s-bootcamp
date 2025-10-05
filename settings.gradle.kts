rootProject.name = "k8s-bootcamp"

include("common-lib")
include("backend-app")
include("management-app")

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
  includeBuild("gradle/conventions")
}