package com.erval.argos.process.bootstrap.config;

import com.erval.argos.contracts.resource.v1.ResourceQueryServiceGrpc;
import com.erval.argos.process.adapters.grpc.GrpcResourceQueryAdapter;
import com.erval.argos.process.adapters.rabbitmq.publisher.RabbitReportRequestPublisherAdapter;
import com.erval.argos.process.application.port.in.StartReportJobUseCase;
import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.application.port.out.ReportRequestPublisherPort;
import com.erval.argos.process.application.port.out.ResourceQueryPort;
import com.erval.argos.process.application.service.JobService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public void close() {
        if (resourceChannel == null) return;
        resourceChannel.shutdown();
        try {
            resourceChannel.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
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
    public ReportRequestPublisherPort reportRequestPublisherPort(RabbitTemplate rabbitTemplate) {
        return new RabbitReportRequestPublisherAdapter(rabbitTemplate);
    }

    @Bean
    public JobService startReportJobUseCase(
        ProcessJobRepositoryPort repo,
        ResourceQueryPort resourceQuery,
        ReportRequestPublisherPort publisher) {
        return new JobService(repo, resourceQuery, publisher);
    }

}
