package com.erval.argos.process.core.domain.job;

import java.time.Instant;

public record ProcessJob(
    String id,
    JobType type,
    String deviceId,
    String deviceName,
    JobStatus status,
    Instant createdAt) {

    public ProcessJob withStatus(JobStatus newStatus) {
        return new ProcessJob(id, type, deviceId, deviceName, newStatus, createdAt);
    }
}
