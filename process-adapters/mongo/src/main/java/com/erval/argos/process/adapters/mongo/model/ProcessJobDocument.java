package com.erval.argos.process.adapters.mongo.model;

import java.time.Instant;

import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("process_jobs")
public record ProcessJobDocument(
        @Id String id,
        JobType jobType,
        String deviceId,
        JobStatus status,
        Instant createdAt) {

    public static ProcessJobDocument fromDomain(ProcessJob job) {
        return new ProcessJobDocument(job.id(), job.type(), job.deviceId(), job.status(), job.createdAt());
    }

    public ProcessJob toDomain() {
        return new ProcessJob(id, jobType, deviceId, status, createdAt);
    }
}
