import java.net.URI

plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
}

group = "helius.systems"
version = "3.0.0-SNAPSHOT"

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

java {
    withJavadocJar()
    withSourcesJar()
}

val isReleaseVersion = !(version as String).endsWith("SNAPSHOT")

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group as String
            artifactId = rootProject.name
            version = version
            from(components["java"])

            pom {
                name = "Factorium"
                description = "A library made to simplify the generation of testing fixtures."
                url = "https://github.com/SBeausoleil/Factorium"
                packaging = "jar"
                licenses {
                    license {
                        name = "MIT Licence"
                        url = "https://github.com/SBeausoleil/Factorium/blob/main/LICENSE.md"
                    }
                }
                developers {
                    developer {
                        id = "samuelb"
                        name = "Samuel Beausoleil"
                        email = "sbeausoleil_dev@hotmail.com"
                        organization = "Independent"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/SBeausoleil/Factorium.git"
                    developerConnection = "scm:git:ssh://github.com:SBeausoleil/Factorium.git"
                    url = "https://github.com/SBeausoleil/Factorium/tree/main"
                }
            }
        }
    }
    repositories {
        maven {
            val releaseRepo = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotRepo = URI("https://s01.oss.sonatype.org/content/repositories/snapshots/")

            name = "OSSRH"
            url = if (isReleaseVersion) releaseRepo else snapshotRepo

            credentials {
                username = properties["ossrhUsername"] as String
                password = properties["ossrhPassword"] as String
            }
        }
    }
}