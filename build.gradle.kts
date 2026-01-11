plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "4.0.0" apply false
}

import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

fun loadDotenv(path: String = ".env"): Map<String, String> {
    val file = file(path)
    if (!file.exists()) return emptyMap()
    return file.readLines()
        .asSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() && !it.startsWith("#") }
        .mapNotNull { line ->
            val idx = line.indexOf('=')
            if (idx <= 0) return@mapNotNull null
            val key = line.substring(0, idx).trim()
            val value = line.substring(idx + 1).trim().trim('"')
            if (key.isEmpty()) null else key to value
        }
        .toMap()
}

val dotenv = loadDotenv()

allprojects {
    group = "com.erval.argos"
    version = System.getenv("VERSION") ?: "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ErVaaL/argos-contracts")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: System.getenv("GH_USER")
                    ?: dotenv["GH_USER"]
                password = System.getenv("GITHUB_TOKEN")
                    ?: System.getenv("GH_TOKEN")
                    ?: dotenv["GH_TOKEN"]
            }
        }
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.0")
        }
    }

    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(21))}
    }

    tasks.test {
        useJUnitPlatform()
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:6.0.2"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

val coverageProjects = setOf(
    ":process-application",
    ":process-adapters:grpc",
    ":process-adapters:mongo",
    ":process-adapters:rabbitmq",
    ":process-adapters:excel",
)

subprojects {
    if (path in coverageProjects) {
        apply(plugin = "jacoco")

        tasks.withType<Test>().configureEach {
            finalizedBy("jacocoTestReport")
        }

        tasks.named<JacocoReport>("jacocoTestReport") {
            dependsOn("test")
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }

        tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
            dependsOn("test")
            violationRules {
                rule {
                    limit {
                        minimum = "0.70".toBigDecimal()
                    }
                }
            }
        }

        tasks.named("check") {
            dependsOn("jacocoTestCoverageVerification")
        }
    }
}
