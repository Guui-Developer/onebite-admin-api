import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "dev.onebite"
version = "0.0.1"
description = "dev.onebite for admin"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // 1. AWS Lambda & Spring Boot Core
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot3:2.1.5")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // 2. Spring Cloud AWS - BOM 먼저 추가
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.2.1"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-secrets-manager")  // ✅ 수정됨

    // 3. AWS SDK v2
    implementation(platform("software.amazon.awssdk:bom:2.29.0"))
    implementation("software.amazon.awssdk:secretsmanager")
    implementation("software.amazon.awssdk:rds")

    // 4. DB & Utils
    runtimeOnly("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // 5. Test & Local
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
// BootJar (뚱뚱한 Jar) 끄기
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

// 일반 Jar (껍데기) 끄기
tasks.named<Jar>("jar") {
    enabled = true
    archiveClassifier.set("plain")
}

// ShadowJar 설정 (람다용 Flat Jar)
tasks.named<ShadowJar>("shadowJar") {
    // 파일명 뒤에 -aws가 붙습니다. (예: api-0.0.1-aws.jar)
    archiveClassifier.set("aws")

    configurations = listOf(project.configurations.runtimeClasspath.get())

    // 1. Service 파일 병합 (SPI)
    mergeServiceFiles()

    // 2. 스프링 부트 3 필수 설정 파일 병합
    transform(AppendingTransformer::class.java) {
        resource = "META-INF/spring.handlers"
    }
    transform(AppendingTransformer::class.java) {
        resource = "META-INF/spring.schemas"
    }
    transform(AppendingTransformer::class.java) {
        resource = "META-INF/spring.tooling"
    }
    transform(AppendingTransformer::class.java) {
        resource = "META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports"
    }
    transform(AppendingTransformer::class.java) {
        resource = "META-INF/spring/org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration.imports"
    }


    manifest {
        attributes(
            "Main-Class" to "dev.onebite.admin.AdminApplication",
            "Start-Class" to "dev.onebite.admin.AdminApplication"
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}

// build 실행 시 shadowJar가 실행되도록 연결
tasks.named("assemble") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
