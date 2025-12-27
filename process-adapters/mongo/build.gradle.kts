plugins {
    id("org.springframework.boot") apply false
    `java-library`
}

dependencies {
    implementation(project(":services:process-service:process-application"))
    implementation(project(":services:process-service:process-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}
