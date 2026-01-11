plugins {
    id("org.springframework.boot") apply false
    `java-library`
}

dependencies {
    implementation(project(":process-application"))
    implementation(project(":process-core"))

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:mongodb:1.21.3")
    testImplementation("org.testcontainers:junit-jupiter:1.21.3")
}
