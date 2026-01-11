plugins {
    `java-library`
}

dependencies {
    api(project(":process-core"))

    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
}
