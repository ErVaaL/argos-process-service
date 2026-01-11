package com.erval.argos.process.bootstrap.config;

import com.erval.argos.contracts.resource.v1.ResourceQueryServiceGrpc;
import com.erval.argos.process.adapters.grpc.GrpcResourceQueryAdapter;
import com.erval.argos.process.adapters.excel.ExcelProcessJobExporterAdapter;
import com.erval.argos.process.adapters.rabbitmq.publisher.RabbitReportRequestPublisherAdapter;
import com.erval.argos.process.application.port.in.StartReportJobUseCase;
import com.erval.argos.process.application.port.out.ProcessJobExcelExporterPort;
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

/**
 * Wires application services and external adapters for the process service.
 */
@Configuration
public class ProcessWiringConfig {


    private ManagedChannel resourceChannel;

    /**
     * Creates a gRPC channel to the resource service.
     *
     * @param host resource gRPC host
     * @param port resource gRPC port
     * @return managed channel
     */
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

    /**
     * Closes the gRPC channel on shutdown.
     */
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

    /**
     * Builds a blocking stub for resource queries.
     *
     * @param resourceChannel gRPC channel
     * @return blocking stub
     */
    @Bean
    ResourceQueryServiceGrpc.ResourceQueryServiceBlockingStub resourceStub(ManagedChannel resourceChannel) {
        return ResourceQueryServiceGrpc.newBlockingStub(resourceChannel);
    }

    /**
     * Exposes the resource query port backed by gRPC.
     *
     * @param stub gRPC blocking stub
     * @return resource query port
     */
    @Bean
    public ResourceQueryPort resourceQueryPort(ResourceQueryServiceGrpc.ResourceQueryServiceBlockingStub stub) {
        return new GrpcResourceQueryAdapter(stub);
    }

    /**
     * Exposes the report request publisher backed by RabbitMQ.
     *
     * @param rabbitTemplate Rabbit template
     * @return report request publisher
     */
    @Bean
    public ReportRequestPublisherPort reportRequestPublisherPort(RabbitTemplate rabbitTemplate) {
        return new RabbitReportRequestPublisherAdapter(rabbitTemplate);
    }

    /**
     * Wires the job service use case.
     *
     * @param repo job repository port
     * @param resourceQuery resource query port
     * @param publisher report request publisher
     * @param excelExporter Excel exporter port
     * @return job service instance
     */
    @Bean
    public JobService startReportJobUseCase(
        ProcessJobRepositoryPort repo,
        ResourceQueryPort resourceQuery,
        ReportRequestPublisherPort publisher,
        ProcessJobExcelExporterPort excelExporter) {
        return new JobService(repo, resourceQuery, publisher, excelExporter);
    }

    /**
     * Provides the Excel exporter adapter.
     *
     * @return Excel exporter
     */
    @Bean
    public ProcessJobExcelExporterPort processJobExcelExporterPort() {
        return new ExcelProcessJobExporterAdapter();
    }

}
