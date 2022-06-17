import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "cloud.folium"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")


    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-validation:2.6.4")
    runtimeOnly ("com.h2database:h2")
    runtimeOnly ("org.postgresql:postgresql")

    implementation ("org.hibernate.validator:hibernate-validator:6.2.0.Final")

    testImplementation ("org.testcontainers:postgresql:1.16.3")
    implementation ("io.micrometer:micrometer-registry-prometheus:1.9.0")

    implementation(platform("com.amazonaws:aws-java-sdk-bom:1.11.1000"))
    implementation("com.amazonaws:aws-java-sdk-s3")

    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.7")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    testImplementation("io.kotest:kotest-runner-junit5:5.3.1")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    implementation("io.kotest.extensions:kotest-extensions-spring:1.1.0")
    implementation("io.kotest:kotest-extensions-testcontainers-jvm:4.4.3")
    implementation("io.kotest:kotest-extensions-mockserver:4.4.3")
    implementation("com.google.code.gson:gson:2.8.6")
    testImplementation("org.springframework:spring-test:5.3.21")

    implementation("org.springframework.security:spring-security-test:5.5.6")



}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
