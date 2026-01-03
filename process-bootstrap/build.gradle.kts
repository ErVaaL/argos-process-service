plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":services:process-service:process-application"))
    implementation(project(":services:process-service:process-adapters:grpc"))
    implementation(project(":services:process-service:process-adapters:mongo"))
    implementation(project(":services:process-service:process-adapters:rabbitmq"))

    implementation("org.springframework.boot:spring-boot-starter-amqp")
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
}
