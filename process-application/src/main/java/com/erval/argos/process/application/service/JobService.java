package com.erval.argos.process.application.service;

import java.time.Instant;
import java.util.UUID;

import com.erval.argos.process.application.port.in.StartReportJobUseCase;
import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.application.port.out.ReportRequestPublisherPort;
import com.erval.argos.process.application.port.out.ResourceQueryPort;
import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

public record JobService(
    ProcessJobRepositoryPort repo,
    ResourceQueryPort resourceQuery,
    ReportRequestPublisherPort publisher) implements StartReportJobUseCase {

    @Override
    public ProcessJob createReportJob(CreateReportJobCommand cmd) {

        var device = resourceQuery.getDevice(cmd.deviceId());
        if (!device.found())
            throw new IllegalArgumentException("Device not found: " + cmd.deviceId());
        if (!device.active())
            throw new IllegalArgumentException("Device inactive: " + cmd.deviceId());

        var from = cmd.from();
        var to = cmd.to();

        if (from == null || from.isBlank() || to == null || to.isBlank()) {
            var now = Instant.now();
            from = now.minus(java.time.Duration.ofHours(24)).toString();
            to = now.toString();
        }
        // Placeholder; later replaced with grpc-based checks
        String id = UUID.randomUUID().toString();
        ProcessJob job = new ProcessJob(
                id,
                JobType.REPORT_PDF,
                cmd.deviceId(),
                JobStatus.REQUESTED,
                Instant.now());
        var saved = repo.save(job);
        publisher.reportRequested(saved.id(), saved.deviceId(), from, to);
        return saved;
    }
}
