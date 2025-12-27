plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":services:process-service:process-application"))
    implementation(project(":services:process-service:process-adapters:grpc"))
    implementation(project(":services:process-service:process-adapters:mongo"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
