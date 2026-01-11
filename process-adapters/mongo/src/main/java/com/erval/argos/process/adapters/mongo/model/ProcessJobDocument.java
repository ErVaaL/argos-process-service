package com.erval.argos.process.adapters.mongo.model;

import java.time.Instant;

import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB document representation of {@link ProcessJob}.
 *
 * @param id         job identifier
 * @param jobType    job type
 * @param deviceId   target device id
 * @param deviceName target device name
 * @param status     job status
 * @param createdAt  creation timestamp
 */
@Document("process_jobs")
public record ProcessJobDocument(
        @Id String id,
        JobType jobType,
        String deviceId,
        String deviceName,
        JobStatus status,
        Instant createdAt) {

    /**
     * Maps a domain job into a Mongo document.
     *
     * @param job domain job aggregate
     * @return document representation
     */
    public static ProcessJobDocument fromDomain(ProcessJob job) {
        return new ProcessJobDocument(job.id(), job.type(), job.deviceId(), job.deviceName(), job.status(),
                job.createdAt());
    }

    /**
     * Maps this document into a domain job aggregate.
     *
     * @return domain job
     */
    public ProcessJob toDomain() {
        return new ProcessJob(id, jobType, deviceId, deviceName, status, createdAt);
    }
}
