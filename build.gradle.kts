import org.gradle.api.JavaVersion.VERSION_11

plugins {
	java
	id("org.springframework.boot") version("2.5.6")
	id("io.spring.dependency-management") version("1.0.11.RELEASE")
}

group = "xyz.oliwer"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	// shade
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains:annotations:20.1.0")
	implementation("com.github.twitch4j:twitch4j:1.5.0")
	implementation("com.jsoniter:jsoniter:0.9.1")

	// test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
	useJUnitPlatform()
}

java {
	sourceCompatibility = VERSION_11
	targetCompatibility = VERSION_11
}