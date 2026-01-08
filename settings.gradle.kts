rootProject.name = "argos-v2-process-service"

include(
    ":process-core",
    ":process-application",
    ":process-adapters:grpc",
    ":process-adapters:mongo",
    ":process-adapters:rabbitmq",
    ":process-adapters:excel",
    ":process-bootstrap",
)

