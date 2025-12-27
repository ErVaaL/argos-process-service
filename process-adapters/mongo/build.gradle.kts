plugins {
    id("org.springframework.boot") apply false
    `java-library`
}

dependencies {
    implementation(project(":services:process-service:process-application"))
    implementation(project(":services:process-service:process-core"))

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}
