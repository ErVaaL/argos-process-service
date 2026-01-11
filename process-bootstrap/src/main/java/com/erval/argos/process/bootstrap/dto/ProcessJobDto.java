package com.erval.argos.process.bootstrap.dto;

import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

/**
 * REST-facing DTO for process job data.
 *
 * @param id        job identifier
 * @param type      job type
 * @param deviceId  device identifier
 * @param deviceName device name
 * @param status    job status
 * @param createdAt ISO-8601 creation timestamp
 */
public record ProcessJobDto(
        String id,
        JobType type,
        String deviceId,
        String deviceName,
        JobStatus status,
        String createdAt) {
    /**
     * Maps a domain job to its DTO representation.
     *
     * @param job domain job
     * @return DTO for the job
     */
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
