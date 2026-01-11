package com.erval.argos.process.application.port.in;

import com.erval.argos.process.core.domain.job.ProcessJob;

/**
 * Use case for creating report jobs.
 */
public interface StartReportJobUseCase {

    /**
     * Starts a report job for a device and date range.
     *
     * @param cmd command containing device id and optional range
     * @return the created job aggregate
     */
    ProcessJob createReportJob(CreateReportJobCommand cmd);

    /**
     * Input command for starting a report job.
     *
     * @param deviceId target device identifier
     * @param from     inclusive start timestamp (ISO-8601) or {@code null}
     * @param to       inclusive end timestamp (ISO-8601) or {@code null}
     */
    record CreateReportJobCommand(String deviceId, String from, String to) {
    }
}
