package com.erval.argos.process.application.port.out;

/**
 * Port for publishing report request events.
 */
public interface ReportRequestPublisherPort {
    /**
     * Publishes a report requested event.
     *
     * @param jobId    job identifier
     * @param deviceId device identifier
     * @param from     range start timestamp (ISO-8601)
     * @param to       range end timestamp (ISO-8601)
     */
    void reportRequested(String jobId, String deviceId, String from, String to);
}
