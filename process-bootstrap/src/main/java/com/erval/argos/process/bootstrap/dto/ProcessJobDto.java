package com.erval.argos.process.bootstrap.dto;

import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

public record ProcessJobDto(
        String id,
        JobType type,
        String deviceId,
        String deviceName,
        JobStatus status,
        String createdAt) {
    public static ProcessJobDto fromDomain(ProcessJob job) {
        return new ProcessJobDto(
                job.id(),
                job.type(),
                job.deviceId(),
                job.deviceName(),
                job.status(),
                job.createdAt().toString());
    }
}
