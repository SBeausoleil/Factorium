plugins {
    id("java-library")
    id("maven-publish")
}

group = "helius.systems"
version = "3.0.0-ALPHA-1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.datafaker:datafaker:2.1.0")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}