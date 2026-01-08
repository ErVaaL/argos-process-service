plugins {
    `java-library`
}

dependencies {
    implementation(project(":process-application"))
    implementation("org.apache.poi:poi-ooxml:5.5.1")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
    implementation("io.grpc:grpc-netty-shaded:1.78.0")
}
