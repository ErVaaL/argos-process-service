package com.erval.argos.process.application.service;

import java.time.Instant;
import java.util.UUID;

import com.erval.argos.process.application.port.in.QueryReportJobsUseCase;
import com.erval.argos.process.application.port.in.StartReportJobUseCase;
import com.erval.argos.process.application.port.out.ProcessJobExcelExporterPort;
import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.application.port.out.ReportRequestPublisherPort;
import com.erval.argos.process.application.port.out.ResourceQueryPort;
import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

public record JobService(
    ProcessJobRepositoryPort repo,
    ResourceQueryPort resourceQuery,
    ReportRequestPublisherPort publisher,
    ProcessJobExcelExporterPort excelExporter) implements StartReportJobUseCase, QueryReportJobsUseCase {

  @Override
  public byte[] exportPerformedJobs(String filter) {
    var jobs = repo.findAll();
    if (filter != null && !filter.isBlank()) {
      var normalized = filter.trim().toUpperCase();
      JobStatus statusFilter = null;
      try {
        statusFilter = JobStatus.valueOf(normalized);
      } catch (IllegalArgumentException ignored) {
        statusFilter = null;
      }
      if (statusFilter != null) {
        JobStatus finalStatusFilter = statusFilter;
        jobs = jobs.stream().filter(job -> job.status() == finalStatusFilter).toList();
      }
    }
    return excelExporter.export(jobs);
  }

  @Override
  public PageResult<ProcessJob> listReportJobs(PageRequest pageRequest) {
    return repo.findAll(pageRequest);
  }

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
        device.id(),
        device.name(),
        JobStatus.REQUESTED,
        Instant.now());
    var saved = repo.save(job);
    publisher.reportRequested(saved.id(), saved.deviceId(), from, to);
    return saved;
  }
}
