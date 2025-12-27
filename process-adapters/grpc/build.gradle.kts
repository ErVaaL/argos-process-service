plugins {
    `java-library`
}

dependencies {
    implementation(project(":services:process-service:process-application"))
    api(project(":contracts"))

    implementation("io.grpc:grpc-netty-shaded:1.78.0")
}
