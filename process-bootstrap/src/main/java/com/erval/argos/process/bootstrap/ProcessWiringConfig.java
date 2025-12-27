package com.erval.argos.process.bootstrap;

import com.erval.argos.contracts.resource.v1.ResourceQueryServiceGrpc;
import com.erval.argos.process.adapters.grpc.GrpcResourceQueryAdapter;
import com.erval.argos.process.application.port.in.StartReportJobUseCase;
import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.application.port.out.ResourceQueryPort;
import com.erval.argos.process.application.service.JobService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ProcessWiringConfig {


    private ManagedChannel resourceChannel;

    @Bean
    public ManagedChannel resourceChannel(
        @Value("${argos.resource.grpc.host:localhost}") String host,
        @Value("${argos.resource.grpc.port:9091}") int port
    ) {
        this.resourceChannel =
            ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        return this.resourceChannel;
    }

    @PreDestroy
    public void close() throws InterruptedException {
        if (resourceChannel != null) {
            resourceChannel.shutdown();
            resourceChannel.awaitTermination(3, TimeUnit.SECONDS);
            resourceChannel.shutdownNow();
        }
    }

    @Bean
    ResourceQueryServiceGrpc.ResourceQueryServiceBlockingStub resourceStub(ManagedChannel resourceChannel) {
        return ResourceQueryServiceGrpc.newBlockingStub(resourceChannel);
    }

    @Bean
    public ResourceQueryPort resourceQueryPort(ResourceQueryServiceGrpc.ResourceQueryServiceBlockingStub stub) {
        return new GrpcResourceQueryAdapter(stub);
    }

    @Bean
    public StartReportJobUseCase startReportJobUseCase(
        ProcessJobRepositoryPort repo,
        ResourceQueryPort resourceQuery) {
        return new JobService(repo, resourceQuery);

    }

}
