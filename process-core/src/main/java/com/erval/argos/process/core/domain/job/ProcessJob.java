package com.erval.argos.process.core.domain.job;

import java.time.Instant;

/**
 * Represents a report-processing job tracked by the process service.
 *
 * @param id         unique job identifier
 * @param type       type of job requested
 * @param deviceId   identifier of the target device
 * @param deviceName human-readable device name at request time
 * @param status     current job status
 * @param createdAt  timestamp when the job was created
 */
public record ProcessJob(
    String id,
    JobType type,
    String deviceId,
    String deviceName,
    JobStatus status,
    Instant createdAt) {

    /**
     * Returns a copy of this job with an updated status.
     *
     * @param newStatus new status value
     * @return a new job instance with the updated status
     */
    public ProcessJob withStatus(JobStatus newStatus) {
        return new ProcessJob(id, type, deviceId, deviceName, newStatus, createdAt);
    }
}
