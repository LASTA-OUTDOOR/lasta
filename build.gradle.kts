buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.sonarqube") version "4.4.1.3373"
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

sonar {
    properties {
        property("sonar.projectKey", "LASTA-OUTDOOR_lasta")
        property("sonar.organization", "lasta")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}